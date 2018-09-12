package es.leanmind.reports.integration;

import es.leanmind.reports.helpers.IntegrationTests;
import es.leanmind.reports.domain.Establishment;
import es.leanmind.reports.domain.EstablishmentDTO;
import es.leanmind.reports.domain.FtpUser;
import es.leanmind.reports.domain.WebUser;
import es.leanmind.reports.helpers.Any;
import es.leanmind.reports.persistence.PostgresEstablishmentRepository;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class EstablishmentRepositoryShould extends IntegrationTests {

    private PostgresEstablishmentRepository establishmentRepository;
    private Sql2o sql2o;

    @Before
    public void given_a_repository_and_a_database() {
        establishmentRepository = new PostgresEstablishmentRepository(connectionTestDatabase);
        sql2o = new Sql2o(connectionBaseUrl + testDb, dbUser, dbPassword);
    }

    @Test
    public void finds_user_by_credentials() {
        String hash = "someKindOfHash124asdfavas3rasd";
        String username = "test";
        try(Connection connection = sql2o.open()) {
            connection.createQuery(
                    "INSERT INTO webusers(username, passwordhash) values (:username, :passwordhash)")
                    .addParameter("username", username)
                    .addParameter("passwordhash", hash)
                    .executeUpdate();
        }

        assertThat(establishmentRepository.getWebUserHashedPassword(username)).isEqualTo(hash);
    }

    @Test
    public void not_create_a_web_user_if_it_already_exists() {
        String username = "Parroty";
        WebUser webUser = new WebUser(username, "Money");
        Establishment establishmentOne = new Establishment(Any.string(), anyFtpUser(), webUser);
        Establishment establishmentTwo = new Establishment(Any.string(), anyFtpUser(), webUser);

        establishmentRepository.create(establishmentOne);
        establishmentRepository.create(establishmentTwo);

        List<String> webUsernamesInDatabase = retrieveWebUsernames();
        assertThat(webUsernamesInDatabase.size()).isEqualTo(1);
        assertThat(webUsernamesInDatabase.get(0)).isEqualTo(username);
    }

    // TODO Establishment requires 1 webuser at least
    @Test
    public void not_create_an_ftp_user_if_it_already_exists() {
        String username = "Parroty";
        FtpUser ftpUser = new FtpUser(username, Any.string(), Any.string());
        Establishment establishmentOne = new Establishment(Any.string(), ftpUser, anyWebUser());
        Establishment establishmentTwo = new Establishment(Any.string(), ftpUser, anyWebUser());

        establishmentRepository.create(establishmentOne);
        establishmentRepository.create(establishmentTwo);

        List<String> ftpUsernamesInDatabase = retrieveFtpUsernames();
        assertThat(ftpUsernamesInDatabase.size()).isEqualTo(1);
        assertThat(ftpUsernamesInDatabase.get(0)).isEqualTo(username);
    }

    @Test
    public void find_all_establishments_for_a_web_user() {
        String establishmentOneName = "Parrot Bar";
        String establishmentTwoName = "Sloth Bar";
        WebUser webUser = new WebUser("Parroty", "Money");
        Establishment establishmentOne = new Establishment(establishmentOneName, anyFtpUser(), webUser);
        Establishment establishmentTwo = new Establishment(establishmentTwoName, anyFtpUser(), webUser);
        establishmentRepository.create(establishmentOne);
        establishmentRepository.create(establishmentTwo);

        List<EstablishmentDTO> retrievedEstablishments = establishmentRepository.retrieveEstablishmentsFor(webUser.getUsername());

        assertThat(retrievedEstablishments.size()).isEqualTo(2);
        assertThat(retrievedEstablishments.get(0).name).isEqualTo(establishmentOneName);
        assertThat(retrievedEstablishments.get(1).name).isEqualTo(establishmentTwoName);
    }

    private FtpUser anyFtpUser() {
        return new FtpUser(Any.string(), Any.string(), Any.string());
    }

    private WebUser anyWebUser() {
        return new WebUser(Any.string(), Any.string());
    }

    private List<String> retrieveFtpUsernames() {
        try(Connection connection = sql2o.open()) {
            return connection.createQuery("SELECT username FROM ftpUsers")
                    .executeScalarList(String.class);
        }
    }

    private List<String> retrieveWebUsernames() {
        try(Connection connection = sql2o.open()) {
            return connection.createQuery("SELECT username FROM webUsers")
                    .executeScalarList(String.class);
        }
    }
}
