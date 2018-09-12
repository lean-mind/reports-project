package es.leanmind.reports.infrastructure;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import es.leanmind.reports.domain.FiltersDateRange;
import es.leanmind.reports.domain.TotalsReportDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TotalsReportsPDFGenerator {

    public void generate(TotalsReportDTO dto, HttpServletResponse response, FiltersDateRange dateRange) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, response.getOutputStream());
        generate(dto, document, dateRange);
        document.close();
    }

    private void generate(TotalsReportDTO dto, Document document, FiltersDateRange dateRange) throws DocumentException {
        document.open();

        Paragraph title = new Paragraph("Report");
        Chapter chapter = new Chapter(title, 1);
        chapter.setNumberDepth(0);

        Paragraph someSectionText = new Paragraph(String.format("Reports for %s", dateFormat(dateRange)));
        chapter.add(someSectionText);

        PdfPTable table = new PdfPTable(10);
        table.setSpacingBefore(25);
        table.setSpacingAfter(25);

        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.WHITE);
        PdfPCell h1 = createFormattedCell(new Phrase("Local", boldFont));
        PdfPCell h2 = createFormattedCell(new Phrase("Número de Facturas", boldFont));
        PdfPCell h3 = createFormattedCell(new Phrase("Importe Total sin Impuestos", boldFont));
        PdfPCell h4 = createFormattedCell(new Phrase("Importe Total con Impuestos", boldFont));
        PdfPCell h5 = createFormattedCell(new Phrase("Importe Medio con Impuestos", boldFont));
        BaseColor blue = new BaseColor(165, 196, 233, 1);
        h1.setBackgroundColor(blue);
        h2.setBackgroundColor(blue);
        h3.setBackgroundColor(blue);
        h4.setBackgroundColor(blue);
        h5.setBackgroundColor(blue);
        table.addCell(h1);
        table.addCell(h2);
        table.addCell(h3);
        table.addCell(h4);
        table.addCell(h5);

        dto.establishments.forEach(establishment -> {
            table.addCell(createFormattedCell(establishment.name));
            table.addCell(createFormattedCell(establishment.numberOfInvoices.toString()));
            table.addCell(createFormattedCell(establishment.netTotals.toString()));
            table.addCell(createFormattedCell(establishment.grossTotals.toString()));
            table.addCell(createFormattedCell(establishment.averageGrossTotals.toString()));
        });

        PdfPCell f1 = createFormattedCell(new Phrase("TOTAL"));
        PdfPCell f2 = createFormattedCell(new Phrase(dto.totalNumberOfInvoices.toString()));
        PdfPCell f3 = createFormattedCell(new Phrase(dto.totalNetAmount));
        PdfPCell f4 = createFormattedCell(new Phrase(dto.totalGrossAmount));
        PdfPCell f5 = createFormattedCell(new Phrase(dto.totalAverageGrossTotals));
        f1.setBackgroundColor(blue);
        f2.setBackgroundColor(blue);
        f3.setBackgroundColor(blue);
        f4.setBackgroundColor(blue);
        f5.setBackgroundColor(blue);
        table.addCell(f1);
        table.addCell(f2);
        table.addCell(f3);
        table.addCell(f4);
        table.addCell(f5);

        chapter.add(table);
        document.add(chapter);
    }

    private PdfPCell createFormattedCell(String phrase) {
        return createFormattedCell(new Phrase(phrase));
    }

    private PdfPCell createFormattedCell(Phrase local) {
        PdfPCell cell = new PdfPCell(local);
        cell.setColspan(2);
        return cell;
    }

    private String dateFormat(FiltersDateRange dateRange) {
        LocalDate from = LocalDate.parse(dateRange.businessDayFrom);
        LocalDate to = LocalDate.parse(dateRange.businessDayTo);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d yyyy");
        return String.format("%s - %s", from.format(dateFormatter), to.format(dateFormatter));
    }
}
