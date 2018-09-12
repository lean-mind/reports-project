package es.leanmind.reports.integration;

import es.leanmind.reports.domain.*;
import es.leanmind.reports.helpers.IntegrationTests;
import es.leanmind.reports.persistence.PostgresEstablishmentRepository;
import es.leanmind.reports.persistence.PostgresInvoiceRepository;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class TotalReportsShould extends IntegrationTests {

    private FtpUser ftpUser1;
    private Establishment establishment1;

    @Before
    public void additionalSetUp(){
        EstablishmentRepository establishmentRepository =
                new PostgresEstablishmentRepository(connectionTestDatabase);
        ftpUser1 = new FtpUser("ftpuser1", "foo", "home");
        WebUser webUser = new WebUser("webuser", "foo");
        establishment1 = new Establishment("Est_1", ftpUser1, webUser);
        establishmentRepository.create(establishment1);
    }

    @Test
    public void generate_a_totals_report() throws Exception {
        InvoiceFileReader reader = new InvoiceFileReader();
        InvoiceRepository invoiceRepository = new PostgresInvoiceRepository(connectionTestDatabase);
        InvoiceService invoiceService = new InvoiceService(invoiceRepository, reader);
        invoiceService.setProcessedFilesFolder("test_resources");
        String grossAmount = "5.50";
        String netAmount = "5.00";
        String establishmentId = "1";
        String dateFrom = "2016-10-02";
        String dateTo = "2016-10-03";
        String invoiceFilePath = "./src/test/test_resources/20170109_191620170109_191153_015_Invoice-T-000384.xml";

        invoiceService.store(invoiceFilePath, ftpUser1.getUsername());
        ReportService service = new ReportService(invoiceRepository);
        ReportFilters filters = new ReportFilters(dateFrom, dateTo, asList(establishmentId));
        TotalsReport report = service.generateReport(filters);

        assertThat(report.totalsByEstablishment.size()).isEqualTo(1);
        assertThat(report.totalNumberOfInvoices()).isEqualTo(1);
        assertThat(report.totalGrossAmount()).isEqualTo(grossAmount);
        assertThat(report.totalNetAmount()).isEqualTo(netAmount);
        assertThat(report.totalAverageGrossAmount()).isEqualTo("5.50");
    }

}
