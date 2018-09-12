package es.leanmind.reports.e2e;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import es.leanmind.reports.domain.Credentials;
import es.leanmind.reports.domain.EstablishmentService;
import es.leanmind.reports.helpers.IntegrationTests;
import es.leanmind.reports.helpers.Properties;
import es.leanmind.reports.helpers.TestFactory;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.jibble.simpleftp.SimpleFTP;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

import static java.nio.file.FileSystems.getDefault;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "spring.datasource.url=jdbc:postgresql://localhost:5432/test_reports?user=reports&password=12345",
                "ftp.port=" + Properties.FTP_SERVER_PORT,
                "server.port=" + Properties.WEB_SERVER_PORT,
                "ftp.home-directory="+ Properties.SYSTEM_PROPERTY,
                "ftp.destination-folder="+ Properties.testFtpFolderName,
                "ftp.destination-processed-folder="+ Properties.testFtpProcessedFilesFolderName
        })
@RunWith(SpringJUnit4ClassRunner.class)
public class GenerateATotalsReport extends IntegrationTests {

    private final String webUrl = "http://localhost:" + Properties.WEB_SERVER_PORT;
    private final String loginUrl = "/login";
    private final String totalsReportUrl = "/reports-totals";

    private final String webUsername = "web@t.com";
    private final String webPassword = "1234";
    private final String establishmentName = "Biotika";

    private final String ftpUsername = "ftp@lacucharasana.es";
    private final String ftpPassword = "lacucharasana";
    private final String ftpUserFolder = "1 - La cuchara sana";

    private final String ftpHost = "127.0.0.1";
    private final Integer ftpPort = Integer.parseInt(Properties.FTP_SERVER_PORT);

    private WebDriver driver;
    private EstablishmentService establishmentService = TestFactory.establishmentService(connectionTestDatabase);

    private WebDriver browser() {
        if (driver == null) {
            ChromeDriverManager.getInstance().setup();
            driver = new ChromeDriver();
        }
        // apparently FirefoxDriver does not support latest Firefox version
        return driver;
    }

    @After
    public void tearDown() {
        driver.close();
    }

    @Test
    public void generate_a_totals_report() throws IOException {
        createEstablishment();
        uploadFileToFtp("20170109_191620170109_191153_015_Invoice-T-000384.xml");
        doWebLogin(webUsername, webPassword);
        // Notice that there are two processes in parallel,
        // the test could fail if the first has not finished
        // when running the query. But for now we leave it
        // like that because it is very unlikely.
        assertThatTotalsReportContainsGross("5.50", "2016-10-02");
    }

    private void assertThatTotalsReportContainsGross(String amount, String dateFrom) {
        Integer establishmentId = establishmentService.retrieveEstablishmentsFor(webUsername).get(0).id;
        String reportArguments = "?establishments[]=" + establishmentId;
        reportArguments += "&fromDay=" + dateFrom;
        reportArguments += "&toDay=" + dateFrom;
        browser().get(webUrl + totalsReportUrl + reportArguments);
        waitForElementWithId("download-buttons", browser());
        try {
            browser().wait(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(browser().getPageSource()).contains(amount);
    }

    protected void uploadFileToFtp(String invoiceFilename) throws IOException {
        SimpleFTP ftp = new SimpleFTP(new SystemStreamLog());
        ftp.connect(ftpHost, ftpPort, ftpUsername, ftpPassword);
        ftp.bin(); // set binary mode
        ftp.stor(fixtureFile(invoiceFilename));
        ftp.disconnect();
    }

    private void doWebLogin(String username, String password) {
        browser().get(webUrl + loginUrl);
        browser().findElement(By.name("username")).sendKeys(username);
        browser().findElement(By.name("password")).sendKeys(password);
        browser().findElement(By.name("submitLogin")).click();
    }

    private void createEstablishment() {
        Credentials ftpCredentials = new Credentials(ftpUsername, ftpPassword);
        Credentials webCredentials = new Credentials(webUsername, webPassword);
        establishmentService.create(establishmentName, ftpCredentials, ftpUserFolder, webCredentials);
    }

    private File fixtureFile(String invoiceFilename) {
        return new File("src/test/test_resources" +
                getDefault().getSeparator()
                + invoiceFilename);
    }

    private void waitForElementWithId(String className, WebDriver browser) {
        WebDriverWait waiter = new WebDriverWait(browser, 5);
        waiter.until(presenceOfElementLocated(By.id(className)));
    }
}
