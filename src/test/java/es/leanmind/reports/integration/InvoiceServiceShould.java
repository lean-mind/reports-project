package es.leanmind.reports.integration;

import es.leanmind.reports.domain.InvoiceFileReader;
import es.leanmind.reports.domain.InvoiceService;
import es.leanmind.reports.helpers.IntegrationTests;
import es.leanmind.reports.helpers.Any;
import es.leanmind.reports.persistence.PostgresInvoiceRepository;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.FileSystems.getDefault;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class InvoiceServiceShould extends IntegrationTests {

    private InvoiceService invoiceService;
    private final String processedFolderName = "Processed";
    private final String xmlInvalidFileName = "invalid.xml";
    private final String xmlInvoiceFileName = "20170109_191620170109_191153_015_Invoice-T-000384.xml";
    private final String testResourcesFolderPath = "src/test/test_resources";
    private final File establishmentFolder = new File(testResourcesFolderPath + getDefault().getSeparator() + "1 - Parrot Bar");

    @Before
    public void before() throws IOException {
        establishmentFolder.mkdirs();
        String invoiceFilePath = testResourcesFolderPath + getDefault().getSeparator() + xmlInvoiceFileName;
        String invalidFilePath = testResourcesFolderPath + getDefault().getSeparator() + xmlInvalidFileName;

        Files.copy(Paths.get(invoiceFilePath), Paths.get( establishmentFolder.toString() + getDefault().getSeparator() + xmlInvoiceFileName));
        Files.copy(Paths.get(invalidFilePath), Paths.get( establishmentFolder.toString() + getDefault().getSeparator() + xmlInvalidFileName));

        invoiceService = new InvoiceService(new PostgresInvoiceRepository(connectionTestDatabase), new InvoiceFileReader());
    }

    @After
    public void after() throws IOException {
        FileUtils.deleteDirectory(establishmentFolder);
    }

    @Test
    public void move_an_uploaded_invoice_file_that_has_been_processed() throws Exception {
        File uploadedXmlInvoice = new File(establishmentFolder +
                getDefault().getSeparator() + xmlInvoiceFileName);
        File processedXmlInvoice = new File(establishmentFolder +
                getDefault().getSeparator() + processedFolderName +
                getDefault().getSeparator() + xmlInvoiceFileName);

        invoiceService.store(uploadedXmlInvoice.toString(), Any.string());

        assertThat(uploadedXmlInvoice.exists()).isFalse();
        assertThat(processedXmlInvoice.exists()).isTrue();
    }

    @Test
    public void move_an_uploaded_file_that_wasnt_an_invoice_to_the_processed_files_folder() throws Exception {
        File uploadedInvalidXmlFile = new File(establishmentFolder +
                getDefault().getSeparator() +  xmlInvalidFileName);
        File processedInvalidXmlFile = new File(establishmentFolder +
                getDefault().getSeparator() + processedFolderName +
                getDefault().getSeparator() + xmlInvalidFileName);

        invoiceService.store(uploadedInvalidXmlFile.toString(), Any.string());

        assertThat(uploadedInvalidXmlFile.exists()).isFalse();
        assertThat(processedInvalidXmlFile.exists()).isTrue();
    }
}
