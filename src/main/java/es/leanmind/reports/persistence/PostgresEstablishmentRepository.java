package es.leanmind.reports.persistence;

import es.leanmind.reports.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class PostgresEstablishmentRepository implements EstablishmentRepository {

    private Sql2o sql2o;

    public PostgresEstablishmentRepository(String connectionUrl) {
        sql2o = new Sql2o(connectionUrl, "reports", "12345");
    }

    @Override
    public void create(Establishment establishment) {
        String query = "INSERT INTO establishments(name) VALUES (:name)";

        try (Connection connection = sql2o.open()) {
            Integer establishmentId = (Integer) connection.createQuery(query, true)
                    .addParameter("name", establishment.name())
                    .executeUpdate()
                    .getKey();

            createAndRelateOne(establishment.ftpUser(), establishmentId, connection);
            createAndRelateMany(establishment.webUsers(), establishmentId, connection);
        }
    }

    private void createAndRelateOne(FtpUser ftpUser, Integer establishmentId, Connection connection) {
        String query = "INSERT INTO ftpusers(establishment, username, passwordhash, homefolder) " +
                       "VALUES (:establishment, :username, :passwordhash, :homefolder)";

        if (ftpUserDoesNotExist(ftpUser.getUsername(), connection)) {
            connection.createQuery(query)
                    .addParameter("establishment", establishmentId)
                    .addParameter("username", ftpUser.getUsername())
                    .addParameter("passwordhash", ftpUser.getPasswordHash())
                    .addParameter("homefolder", ftpUser.getHomeFolder())
                    .executeUpdate();
        }
    }

    private void createAndRelateMany(Set<WebUser> webUsers, Integer establishmentId, Connection connection) {
        String query = "INSERT INTO webusers(username, passwordhash) " +
                       "VALUES (:username, :passwordhash)";

        webUsers.forEach(webUser -> {
            if (webUserDoesNotExist(webUser.getUsername(), connection)) {
                Integer webUserId = (Integer) connection.createQuery(query, true)
                        .addParameter("username", webUser.getUsername())
                        .addParameter("passwordhash", webUser.getPasswordHash())
                        .executeUpdate()
                        .getKey();
                createManyToManyRelation(webUserId, establishmentId);
            } else {
                createManyToManyRelation(retrieveIdFor(webUser.getUsername()), establishmentId);
            }
        });
    }

    private void createManyToManyRelation(Integer webUserId, Integer establishmentId) {
        String query = "INSERT INTO establishments_webusers(establishment, webuser) " +
                       "VALUES (:establishmentId, :webUserId)";

        try(Connection connection = sql2o.open()) {
            connection.createQuery(query)
                    .addParameter("webUserId", webUserId)
                    .addParameter("establishmentId", establishmentId)
                    .executeUpdate();
        }
    }

    private boolean ftpUserDoesNotExist(String username, Connection connection) {
        return connection.createQuery("SELECT username FROM ftpUsers WHERE username = :username")
                        .addParameter("username", username)
                        .executeScalarList(String.class).size() == 0;
    }

    private boolean webUserDoesNotExist(String username, Connection connection) {
        return connection.createQuery("SELECT username FROM webUsers WHERE username = :username")
                .addParameter("username", username)
                .executeScalarList(String.class).size() == 0;
    }

    @Override
    public List<EstablishmentDTO> retrieveEstablishmentsFor(String username) {
        Integer webUserId = retrieveIdFor(username);
        String query = "SELECT establishments.id, establishments.name " +
                       "FROM establishments " +
                       "LEFT JOIN establishments_webusers ON establishments.id = establishments_webusers.establishment " +
                       "WHERE establishments_webusers.webUser = :webUserId";

        List<Map<String, Object>> queryResults;
        try(Connection connection = sql2o.open()) {
            queryResults = connection.createQuery(query)
                    .addParameter("webUserId", webUserId)
                    .executeAndFetchTable()
                    .asList();
        }

        return queryResults.stream()
                .map(this::toEstablishmentDTO)
                .collect(toList());
    }

    private EstablishmentDTO toEstablishmentDTO(Map<String, Object> result) {
        return new EstablishmentDTO((Integer) result.get("id"), (String) result.get("name"));
    }

    private Integer retrieveIdFor(String username) {
        try(Connection connection = sql2o.open()) {
            return connection.createQuery("SELECT id FROM webUsers WHERE username = :username")
                    .addParameter("username", username)
                    .executeScalarList(Integer.class).get(0);
        }
    }

    @Override
    public String getFtpUserHashedPassword(String username) {
        return hashedPasswordFor(username, "ftpUsers");
    }

    @Override
    public FtpUser retrieveFtpUser(String username) {
        String query = "SELECT username, homefolder, passwordhash FROM ftpUsers " +
                "WHERE username = :username";

        FtpUser ftpUser;
        try(Connection connection = sql2o.open()) {
            ftpUser = connection.createQuery(query)
                    .throwOnMappingFailure(false)
                    .addParameter("username", username)
                    .executeAndFetchFirst(FtpUser.class);
        }

        return ftpUser;
    }

    @Override
    public String getWebUserHashedPassword(String username) {
        return hashedPasswordFor(username, "webUsers");
    }

    private String hashedPasswordFor(String username, final String usersTable) {
        String query = "SELECT passwordhash " +
                "FROM " + usersTable + " " +
                "WHERE username = :username";

        List<String> matchingHashedPasswords;
        try(Connection connection = sql2o.open()) {
            matchingHashedPasswords = connection.createQuery(query)
                    .addParameter("username", username)
                    .executeScalarList(String.class);
        }

        if (matchingHashedPasswords.size() != 1){
            throw new UsernameNotFoundException(username);
        }

        return matchingHashedPasswords.get(0);
    }
}