package es.leanmind.reports.domain;

import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceFileReader {

    public UploadedInvoices readInvoices(String path, String ftpUsername) throws Exception {
        Serializer xmlSerializer = new Persister();
        File xml = new File(path);

        InvoicesMap mappedInvoices = xmlSerializer.read(InvoicesMap.class, xml);
        return mappedInvoices.toInvoices(ftpUsername);
    }
}

class InvoicesMap {

    @ElementList
    List<InvoiceMap> Invoices;

    public UploadedInvoices toInvoices(String ftpUsername) {
        List<UploadedInvoice> items = this.Invoices.stream()
                .map((invoice) -> new UploadedInvoice(invoice.BusinessDay, invoice.Date, invoice.Totals.GrossAmount, invoice.Totals.NetAmount))
                .collect(Collectors.toList());
        UploadedInvoices result = new UploadedInvoices(items, ftpUsername);
        return result;
    }
}

@Root(strict = false)
class InvoiceMap {

    @Attribute
    public String BusinessDay;
    @Attribute
    public String Date;
    @Element
    public Totals Totals;
}

@Root(strict = false)
class Totals {

    @Attribute
    public String GrossAmount;
    @Attribute
    public String NetAmount;
}