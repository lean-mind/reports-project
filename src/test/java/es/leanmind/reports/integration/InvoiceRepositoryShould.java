package es.leanmind.reports.integration;

import es.leanmind.reports.domain.*;
import es.leanmind.reports.helpers.IntegrationTests;
import es.leanmind.reports.persistence.PostgresEstablishmentRepository;
import es.leanmind.reports.persistence.PostgresInvoiceRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/* TODO
    Check that we are not saving duplicate invoices (serialNumber)
*/

public class InvoiceRepositoryShould extends IntegrationTests {

    private PostgresInvoiceRepository invoiceRepository;
    private Establishment establishment1;
    private Establishment establishment2;
    private FtpUser ftpUser1;
    private FtpUser ftpUser2;
    private WebUser webUser;

    @Before
    public void given_an_invoice_repository() {
        invoiceRepository = new PostgresInvoiceRepository(connectionTestDatabase);
        EstablishmentRepository establishmentRepository =
                new PostgresEstablishmentRepository(connectionTestDatabase);
        ftpUser1 = new FtpUser("ftpuser1", "foo", "home");
        ftpUser2 = new FtpUser("ftpuser2", "foo", "home");
        webUser = new WebUser("webuser", "foo");
        establishment1 = new Establishment("Est_1", ftpUser1, webUser);
        establishment2 = new Establishment("Est_2", ftpUser2, webUser);
        establishmentRepository.create(establishment1);
        establishmentRepository.create(establishment2);
    }

    @Test
    public void save_multiple_invoices() {
        String establishment1Id = "1"; // am assuming id is 1, fingers crossed
        String establishment2Id = "2"; // am assuming id is 1, fingers crossed
        String businessDayFrom = "2016-10-01";
        String businessDayTo = "2016-10-04";
        String grossAmount1 = "5.50";
        String grossAmount2 = "7.50";
        invoiceRepository.save(
                new UploadedInvoices(Arrays.asList(new UploadedInvoice(
                        "2016-10-02", "2016-10-02T13:45:50",
                        grossAmount1, "5.00")), ftpUser1.getUsername()));
        invoiceRepository.save(
                new UploadedInvoices(Arrays.asList(new UploadedInvoice(
                        "2016-10-03", "2016-10-03T16:22:30",
                        grossAmount2, "7.00")), ftpUser2.getUsername()));

        ReportFilters filters = new ReportFilters(businessDayFrom, businessDayTo,
                Arrays.asList(establishment1Id, establishment2Id));

        InvoicesByEstablishment retrievedInvoices = invoiceRepository.retrieveStoredInvoices(filters);
        assertThat(retrievedInvoices.get(1, 0).grossAmount())
                .isEqualTo(grossAmount1);
        assertThat(retrievedInvoices.get(2, 0).grossAmount())
                .isEqualTo(grossAmount2);
    }

    // TODO: add more tests to cover filtering and grouping
}
