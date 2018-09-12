package es.leanmind.reports.unit;

import es.leanmind.reports.domain.TotalsByEstablishment;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class TotalsByEstablishmentShould {

    @Test
    public void not_divide_by_zero() {
        TotalsByEstablishment zeroValues = new TotalsByEstablishment("anyName", (Integer) 0, new BigDecimal("0"), new BigDecimal("0"));

        assertThat(zeroValues.numberOfInvoices()).isEqualTo(0);
        assertThat(zeroValues.netTotals()).isEqualTo(BigDecimal.ZERO);
        assertThat(zeroValues.grossTotals()).isEqualTo(BigDecimal.ZERO);
        assertThat(zeroValues.averageGrossTotals()).isEqualTo(BigDecimal.ZERO);
    }
}
