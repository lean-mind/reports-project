package es.leanmind.reports.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TotalsByEstablishment {

    public String establishmentName;
    public Integer numberOfInvoices;
    public BigDecimal netTotals;
    public BigDecimal grossTotals;

    public TotalsByEstablishment(String establishmentName, Integer numberOfInvoices, BigDecimal grossTotals, BigDecimal netTotals) {
        this.establishmentName = establishmentName;
        this.numberOfInvoices = numberOfInvoices;
        this.netTotals = netTotals;
        this.grossTotals = grossTotals;
    }

    public BigDecimal grossTotals() {
        return grossTotals;
    }

    public BigDecimal netTotals() {
        return netTotals;
    }

    public Integer numberOfInvoices() {
        return numberOfInvoices;
    }

    public BigDecimal averageGrossTotals() {
        Integer numberOfInvoices = numberOfInvoices();
        if (numberOfInvoices != 0) {
            return grossTotals().divide(new BigDecimal(numberOfInvoices), 2, RoundingMode.HALF_EVEN);
        }
        return BigDecimal.ZERO;
    }
}
