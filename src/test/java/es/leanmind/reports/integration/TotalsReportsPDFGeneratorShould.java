package es.leanmind.reports.integration;

import es.leanmind.reports.domain.TotalsByEstablishmentDTO;
import es.leanmind.reports.domain.TotalsReportDTO;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TotalsReportsPDFGeneratorShould {

    @Test
    public void xxx() throws Exception {

        TotalsReportDTO dto = new TotalsReportDTO();
        dto.totalGrossAmount = "100";
        dto.totalNetAmount = "50";
        dto.totalNumberOfInvoices = 2;
        dto.totalAverageGrossTotals = "50";
        dto.establishments = new ArrayList<>();
        TotalsByEstablishmentDTO e1 = new TotalsByEstablishmentDTO();
        e1.name = "foo";
        e1.grossTotals = BigDecimal.valueOf(20);
        e1.netTotals = BigDecimal.valueOf(10);
        e1.numberOfInvoices = 1;
        e1.averageGrossTotals = BigDecimal.valueOf(20);
        TotalsByEstablishmentDTO e2 = new TotalsByEstablishmentDTO();
        e2.name = "bar";
        e2.grossTotals = BigDecimal.valueOf(20.0);
        e2.netTotals = BigDecimal.valueOf(10);
        e2.numberOfInvoices = 1;
        e2.averageGrossTotals = BigDecimal.valueOf(20);
        dto.establishments.add(e1);
        dto.establishments.add(e2);

        //Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        //PdfWriter.getInstance(document, mock(OutputStream.class));
        //new TotalsReportsPDFGenerator().generate(dto, mock(HttpServletResponse.class), mock(FiltersDateRange.class));
        //document.close();

        // FIXME add test

    }

}
