package es.leanmind.reports.domain;

import java.math.BigDecimal;

public class EstablishmentPeriodData {
    private Integer numberOfInvoices;
    private BigDecimal netTotals;
    private BigDecimal grossTotals;

    public EstablishmentPeriodData() {
        numberOfInvoices = 0;
        netTotals = new BigDecimal(0);
        grossTotals = new BigDecimal(0);
    }

    public Integer numberOfInvoices() {
        return numberOfInvoices;
    }
    public BigDecimal grossTotals() { return grossTotals; }
    public BigDecimal netTotals() { return netTotals; }

    public void addInvoice(StoredInvoice invoice) {
        grossTotals = grossTotals.add(invoice.grossAmount());
        netTotals = netTotals.add(invoice.netAmount());
        numberOfInvoices++;
    }
}
