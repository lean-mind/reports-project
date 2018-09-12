package es.leanmind.reports.infrastructure;

import es.leanmind.reports.domain.TotalsByEstablishmentDTO;
import es.leanmind.reports.domain.TotalsReportDTO;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

public class TotalsReportSpreadsheetGenerator {

    public Workbook generate(TotalsReportDTO dto){
        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet();
        book.setSheetName(0, "Totales");

        HSSFCellStyle headerStyle = book.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0, CellType.STRING);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Total Bruto");
        cell = row.createCell(1, CellType.STRING);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Total Neto");
        cell = row.createCell(2, CellType.STRING);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Número de facturas");

        row = sheet.createRow(1);
        cell = row.createCell(0, CellType.NUMERIC);
        cell.setCellValue(new BigDecimal(dto.totalGrossAmount).doubleValue());
        cell = row.createCell(1, CellType.NUMERIC);
        cell.setCellValue(new BigDecimal(dto.totalNetAmount).doubleValue());
        cell = row.createCell(2, CellType.NUMERIC);
        cell.setCellValue(new BigDecimal(dto.totalNumberOfInvoices).doubleValue());

        row = sheet.createRow(3);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Detalle por establecimiento:");

        cell = row.createCell(0, CellType.STRING);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Detalle por establecimiento");
        cell = row.createCell(1, CellType.STRING);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Nombre");
        cell = row.createCell(2, CellType.STRING);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Total bruto");
        cell = row.createCell(3, CellType.STRING);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Total neto");
        cell = row.createCell(4, CellType.STRING);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Número de facturas");

        int rowIndex = 4;
        for (TotalsByEstablishmentDTO byEstablishment: dto.establishments) {
            row = sheet.createRow(rowIndex);
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(byEstablishment.name);
            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellValue(byEstablishment.grossTotals.doubleValue());
            cell = row.createCell(3, CellType.NUMERIC);
            cell.setCellValue(byEstablishment.netTotals.doubleValue());
            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellValue(byEstablishment.numberOfInvoices);
            rowIndex++;
        }
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }
        return book;
    }

    public void generate(TotalsReportDTO dto, OutputStream outputStream) throws IOException {
        Workbook book = generate(dto);
        book.write(outputStream);
    }
}
