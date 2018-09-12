package es.leanmind.reports.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TotalsReport {

    public List<TotalsByEstablishment> totalsByEstablishment;

    public TotalsReport(List<TotalsByEstablishment> totalsByEstablishment) {
        this.totalsByEstablishment = totalsByEstablishment;
    }

    public TotalsByEstablishment totalsByEstablishmentAt(Integer index) {
        return totalsByEstablishment.get(index);
    }

    public String totalGrossAmount() {
        BigDecimal grossAmount = totalsByEstablishment.stream().map(TotalsByEstablishment::grossTotals).reduce(BigDecimal.ZERO, BigDecimal::add);
        return roundUp(grossAmount).toString();
    }

    public String totalNetAmount() {
        BigDecimal netAmount = totalsByEstablishment.stream().map(TotalsByEstablishment::netTotals).reduce(BigDecimal.ZERO, BigDecimal::add);
        return roundUp(netAmount).toString();
    }

    public Integer totalNumberOfInvoices() {
        return totalsByEstablishment.stream().mapToInt(TotalsByEstablishment::numberOfInvoices).sum();
    }

    public String totalAverageGrossAmount() {
        Integer totalNumberOfInvoices = totalNumberOfInvoices();
        if (totalNumberOfInvoices != 0) {
            BigDecimal averageGross = new BigDecimal(totalGrossAmount())
                    .divide(new BigDecimal(totalNumberOfInvoices), 2, RoundingMode.HALF_EVEN);
            return roundUp(averageGross).toString();
        }
        return roundUp(BigDecimal.ZERO).toString();
    }

    //TODO: extract to helper class
    private BigDecimal roundUp(BigDecimal decimal) {
        return decimal.setScale(2, RoundingMode.HALF_EVEN);
    }
}
