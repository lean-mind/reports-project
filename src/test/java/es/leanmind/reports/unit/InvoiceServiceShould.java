package es.leanmind.reports.unit;

import es.leanmind.reports.domain.*;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class InvoiceServiceShould {

    private String businessDay = "2016-10-02";
    private String date = "2016-10-02T21:41:30";
    private String grossAmount = "5.50";
    private String netAmount = "5.00";
    private String ftpUsername = "foo@bar.com";

    @Test
    public void parse_an_invoice() throws Exception {
        String filePath = "/some-file-path";
        InvoiceFileReader invoiceReader = mock(InvoiceFileReader.class);
        InvoiceRepository invoiceRepository = mock(InvoiceRepository.class);
        InvoiceService invoiceService = new InvoiceService(invoiceRepository, invoiceReader);
        invoiceService.setProcessedFilesFolder("test_resources");
        UploadedInvoice expectedInvoice = new UploadedInvoice(businessDay, date, grossAmount, netAmount);
        UploadedInvoices invoices = new UploadedInvoices(asList(expectedInvoice), ftpUsername);

        when(invoiceReader.readInvoices(filePath, ftpUsername)).thenReturn(invoices);

        invoiceService.store(filePath, ftpUsername);

        ArgumentCaptor<UploadedInvoices> captor = ArgumentCaptor.forClass(UploadedInvoices.class);
        verify(invoiceRepository).save(captor.capture());
        UploadedInvoices actualInvoicesUploaded = captor.getValue();
        assertThat(actualInvoicesUploaded.getItems().get(0)).isEqualTo(expectedInvoice);
        assertThat(actualInvoicesUploaded.ftpUsername()).isEqualTo(ftpUsername);
    }

}