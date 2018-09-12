package es.leanmind.reports.helpers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class IntegrationTests {

    protected static String dbUser = "reports";
    protected static String dbPassword = "12345";
    protected static String connectionBaseUrl = "jdbc:postgresql://localhost:5432/";
    protected static String testDb = "test_reports";
    protected static String connectionTestDatabase = connectionBaseUrl + testDb + "?user=" + dbUser + "&password=" + dbPassword;
    private static String productionDb = "reports";

    @BeforeClass
    public static void createDatabase(){
        Sql2o sql2o = new Sql2o(connectionBaseUrl, dbUser, dbPassword);
        try (Connection connection = sql2o.open()) {
            connection.createQuery("DROP DATABASE IF EXISTS " + testDb)
                    .executeUpdate();
            connection.createQuery("CREATE DATABASE " + testDb + " with TEMPLATE " + productionDb + " OWNER " + dbUser)
                    .executeUpdate();
        }
    }

    @AfterClass
    public static void dropDatabase() {
            Sql2o sql2o = new Sql2o(connectionBaseUrl, dbUser, dbPassword);
            try(Connection connection = sql2o.open()) {
                connection.createQuery("DROP DATABASE IF EXISTS " + testDb)
                        .executeUpdate();
            }
    }

    @Before
    public void setUpCascade() throws Exception {
        flushDatabase();
    }

    @After
    public void tearDownCascade() throws Exception {
        flushDatabase();
    }

    private void flushDatabase() {
        Sql2o sql2o = new Sql2o(connectionBaseUrl + testDb, dbUser, dbPassword);
        try(Connection connection = sql2o.open()) {
            connection.createQuery(
                    "DELETE FROM establishments")
                    .executeUpdate();
            connection.createQuery(
                    "DELETE FROM establishments_webusers")
                    .executeUpdate();
            connection.createQuery(
                    "DELETE FROM ftpusers")
                    .executeUpdate();
            connection.createQuery(
                    "DELETE FROM invoices")
                    .executeUpdate();
            connection.createQuery(
                    "DELETE FROM webusers")
                    .executeUpdate();
        }
    }
}
