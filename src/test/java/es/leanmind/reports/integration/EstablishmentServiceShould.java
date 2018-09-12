package es.leanmind.reports.integration;

import es.leanmind.reports.helpers.IntegrationTests;
import es.leanmind.reports.domain.Credentials;
import es.leanmind.reports.domain.EstablishmentDTO;
import es.leanmind.reports.domain.EstablishmentService;
import es.leanmind.reports.helpers.Any;
import es.leanmind.reports.helpers.TestFactory;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class EstablishmentServiceShould extends IntegrationTests {

    @Test
    public void create_an_organization_with_one_ftp_user_allowed() {
        EstablishmentService establishmentService = TestFactory.establishmentService(connectionTestDatabase);
        Credentials ftpCredentials = new Credentials("ftp_username", "ftp_password");
        Credentials webCredentials = new Credentials("web_username", "web_password");
        String ftpHomeFolder = "ftp_home_folder";
        establishmentService.create("Parrot Bar", ftpCredentials, ftpHomeFolder, webCredentials);

        boolean isFtpUserAllowed = establishmentService.isFtpUserAllowed(ftpCredentials.username, ftpCredentials.password);

        assertThat(isFtpUserAllowed).isTrue();
    }

    @Test
    public void create_an_organization_with_one_web_user_allowed() {
        EstablishmentService establishmentService = TestFactory.establishmentService(connectionTestDatabase);
        Credentials ftpCredentials = new Credentials("ftp_username", "ftp_password");
        Credentials webCredentials = new Credentials("web_username", "web_password");
        String ftpHomeFolder = "ftp_home_folder";
        establishmentService.create("Parrot Bar", ftpCredentials, ftpHomeFolder, webCredentials);

        boolean isWebUserAllowed = establishmentService.isWebUserAllowed(webCredentials.username, webCredentials.password);

        assertThat(isWebUserAllowed).isTrue();
    }

    @Test
    public void retrieve_all_establishments_for_a_web_user_given_its_username_and_password() {
        EstablishmentService establishmentService = TestFactory.establishmentService(connectionTestDatabase);
        String firstEstablishmentName = "First Establishment";
        String secondEstablishmentName = "Second Establishment";
        Credentials webCredentials = new Credentials("web_username", "web_password");
        establishmentService.create(firstEstablishmentName, anyFtpCredentials(), Any.string(), webCredentials);
        establishmentService.create(secondEstablishmentName, anyFtpCredentials(), Any.string(), webCredentials);

        List<EstablishmentDTO> establishments = establishmentService.retrieveEstablishmentsFor(webCredentials.username);

        assertThat(establishments.size()).isEqualTo(2);
        assertThat(establishments.get(0).name).isEqualTo(firstEstablishmentName);
        assertThat(establishments.get(1).name).isEqualTo(secondEstablishmentName);
    }

    private Credentials anyFtpCredentials() {
        return new Credentials(Any.string(), Any.string());
    }
}
