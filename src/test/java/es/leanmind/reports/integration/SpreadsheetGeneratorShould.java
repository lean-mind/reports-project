package es.leanmind.reports.integration;

import es.leanmind.reports.domain.TotalsByEstablishmentDTO;
import es.leanmind.reports.domain.TotalsReportDTO;
import es.leanmind.reports.infrastructure.TotalsReportSpreadsheetGenerator;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SpreadsheetGeneratorShould {

    @Test
    public void generate_totals_report() throws Exception {
        TotalsReportDTO dto = new TotalsReportDTO();
        dto.totalGrossAmount = "100";
        dto.totalNetAmount = "50";
        dto.totalNumberOfInvoices = 2;
        dto.establishments = new ArrayList<>();
        TotalsByEstablishmentDTO e1 = new TotalsByEstablishmentDTO();
        e1.name = "foo";
        e1.grossTotals = BigDecimal.valueOf(20);
        e1.netTotals = BigDecimal.valueOf(10);
        e1.numberOfInvoices = 1;
        TotalsByEstablishmentDTO e2 = new TotalsByEstablishmentDTO();
        e2.name = "bar";
        e2.grossTotals = BigDecimal.valueOf(20.0);
        e2.netTotals = BigDecimal.valueOf(10);
        e2.numberOfInvoices = 1;
        dto.establishments.add(e1);
        dto.establishments.add(e2);

        Workbook book = new TotalsReportSpreadsheetGenerator().generate(dto);
        assertThat(book.getSheetAt(0).getLastRowNum()).isEqualTo(5);
        assertThat(book.getSheetAt(0).getRow(5).getLastCellNum()).isEqualTo(Short.valueOf("5"));
    }
}
