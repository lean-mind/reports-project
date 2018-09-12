package es.leanmind.reports.unit;

import es.leanmind.reports.domain.TotalsByEstablishment;
import es.leanmind.reports.domain.TotalsReport;
import org.junit.Test;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class TotalReportsFormatShould {

    @Test
    public void not_divide_by_zero() {
        TotalsReport zeroValues = new TotalsReport(emptyList());

        assertThat(zeroValues.totalNumberOfInvoices()).isEqualTo(0);
        assertThat(zeroValues.totalNetAmount()).isEqualTo("0.00");
        assertThat(zeroValues.totalGrossAmount()).isEqualTo("0.00");
        assertThat(zeroValues.totalAverageGrossAmount()).isEqualTo("0.00");
    }

    @Test
    public void round_up_with_two_decimals() {
        Integer numberOfInvoices = 2;
        TotalsReport totalsReport = new TotalsReport(asList(
                new TotalsByEstablishment("anyName", numberOfInvoices, new BigDecimal("10.128"), new BigDecimal("10.123"))));


        assertThat(totalsReport.totalNumberOfInvoices()).isEqualTo(numberOfInvoices);
        assertThat(totalsReport.totalNetAmount()).isEqualTo("10.12");
        assertThat(totalsReport.totalGrossAmount()).isEqualTo("10.13");
        assertThat(totalsReport.totalAverageGrossAmount()).isEqualTo("5.06");
    }
}
