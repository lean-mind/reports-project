package es.leanmind.reports.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
public class ReportService {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public ReportService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public TotalsReport generateReport(ReportFilters filters) {
        InvoicesByEstablishment invoices = invoiceRepository.retrieveStoredInvoices(filters);
        return invoices.byEstablishment()
                .map(this::toTotalsByEstablishment)
                .collect(collectingAndThen(toList(), TotalsReport::new));
    }

    public ReportByPeriod generateReportByPeriods(PeriodsReportFilters filters) {
        InvoicesByEstablishment invoices = invoiceRepository.retrieveStoredInvoices(filters);
        ReportByPeriod report = new ReportByPeriod(invoices, filters);
        return report;
    }

    private TotalsByEstablishment toTotalsByEstablishment(List<StoredInvoice> invoiceList) {
        String establishmentName = invoiceList.get(0).establishmentName();
        Integer numberOfInvoices = invoiceList.size();
        BigDecimal grossTotals = invoiceList.stream().map(StoredInvoice::grossAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal netTotals = invoiceList.stream().map(StoredInvoice::netAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new TotalsByEstablishment(establishmentName, numberOfInvoices, grossTotals, netTotals);
    }
}

