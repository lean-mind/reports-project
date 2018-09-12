package es.leanmind.reports.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class InvoiceService {

    private InvoiceRepository invoiceRepository;
    private InvoiceFileReader invoiceReader;

    private String processedFilesFolder;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceFileReader invoiceReader) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceReader = invoiceReader;
    }

    @Value("${ftp.destination-processed-folder}")
    public void setProcessedFilesFolder(String processedFilesFolder) {
        this.processedFilesFolder = processedFilesFolder;
    }

    public void store(String filePath, String ftpUsername) throws IOException {
        try {
            UploadedInvoices invoices = invoiceReader.readInvoices(filePath, ftpUsername);
            invoiceRepository.save(invoices);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileUtils.moveProcessedFile(filePath, processedFilesFolder);
    }
}
