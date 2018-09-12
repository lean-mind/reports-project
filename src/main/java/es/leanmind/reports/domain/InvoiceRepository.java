package es.leanmind.reports.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository {

    void save(UploadedInvoices invoices);

    InvoicesByEstablishment retrieveStoredInvoices(ReportFilters filters);
}
