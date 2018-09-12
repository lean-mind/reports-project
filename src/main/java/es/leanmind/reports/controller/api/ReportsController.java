package es.leanmind.reports.controller.api;

import com.itextpdf.text.DocumentException;
import es.leanmind.reports.domain.*;
import es.leanmind.reports.infrastructure.TotalsReportSpreadsheetGenerator;
import es.leanmind.reports.infrastructure.TotalsReportsPDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@RestController
public class ReportsController {

    private final ReportService reportService;

    @Autowired
    public ReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(path = "/api/reports/json/totals", produces = MediaType.APPLICATION_JSON_VALUE)
    public TotalsReportDTO getTotalsReport(@RequestParam("fromDay") String businessDayFrom,
                                           @RequestParam("toDay") String businessDayTo,
                                           @RequestParam("establishments[]") List<String> establishmentIds) {
        try {
            return getTotalsReportDTO(businessDayFrom, businessDayTo, establishmentIds);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/api/reports/json/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public HistoryReportDTO getHistoryReport(@RequestParam("fromDay") String businessDayFrom,
                                             @RequestParam("toDay") String businessDayTo,
                                             @RequestParam("establishments[]") List<String> establishmentIds,
                                             @RequestParam("groupByPeriod") GroupByPeriod groupByPeriod) {
        try{
            return getReportDTO(businessDayFrom, businessDayTo, establishmentIds, groupByPeriod);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/api/reports/json/hourly", produces = MediaType.APPLICATION_JSON_VALUE)
    public HistoryReportDTO getHourlyReport(@RequestParam("fromDay") String businessDayFrom,
                                             @RequestParam("toDay") String businessDayTo,
                                             @RequestParam("establishments[]") List<String> establishmentIds) {
        try{
            return getReportDTO(businessDayFrom, businessDayTo, establishmentIds, GroupByPeriod.DaysWithHours);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/api/reports/json/grouped-hours", produces = MediaType.APPLICATION_JSON_VALUE)
    public HistoryReportDTO getGroupedHoursReport(@RequestParam("fromDay") String businessDayFrom,
                                            @RequestParam("toDay") String businessDayTo,
                                            @RequestParam("establishments[]") List<String> establishmentIds) {
        try{
            return getReportDTO(businessDayFrom, businessDayTo, establishmentIds, GroupByPeriod.GroupedHours);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/api/reports/pdf/totals", produces = APPLICATION_PDF_VALUE)
    public void getReportPDF(@RequestParam("fromDay") String businessDayFrom,
                             @RequestParam("toDay") String businessDayTo,
                             @RequestParam("establishments[]") List<String> establishmentIds,
                             HttpServletResponse response) throws IOException, DocumentException {
        try {
            response.setContentType(APPLICATION_PDF_VALUE);
            TotalsReportDTO dto = getTotalsReportDTO(businessDayFrom, businessDayTo, establishmentIds);
            new TotalsReportsPDFGenerator().generate(dto, response, new FiltersDateRange(businessDayFrom, businessDayTo));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/api/reports/spreadsheet/totals", produces = MediaType.APPLICATION_XML_VALUE)
    public void getTotalsReportSpreadsheet(@RequestParam("fromDay") String businessDayFrom,
                                           @RequestParam("toDay") String businessDayTo,
                                           @RequestParam("establishments[]") List<String> establishmentIds,
                                           HttpServletResponse response) throws IOException {
        try {
            TotalsReportDTO dto = getTotalsReportDTO(businessDayFrom, businessDayTo, establishmentIds);
            response.setHeader("Content-disposition", "attachment; filename=totales.xls");
            new TotalsReportSpreadsheetGenerator().generate(dto, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private String mapEstablishmentsIdsDTOFilter(List<String> establishmentIds) {
        return establishmentIds.stream()
                .reduce("", (acc, nextId) -> acc += "establishments[]=" + nextId + "&");
    }

    private TotalsReportDTO getTotalsReportDTO(String businessDayFrom, String businessDayTo, List<String> establishmentIds) {
        ReportFilters filters = new ReportFilters(businessDayFrom, businessDayTo, establishmentIds);
        TotalsReportDTO dto = mapToTotalsReportDTO(reportService.generateReport(filters));
        dto.filters = "?" + mapEstablishmentsIdsDTOFilter(establishmentIds)
                + "fromDay=" + businessDayFrom
                + "&toDay=" + businessDayTo;
        return dto;
    }

    private TotalsReportDTO mapToTotalsReportDTO(TotalsReport totalsReport) {
        TotalsReportDTO dto = new TotalsReportDTO();
        dto.totalGrossAmount = totalsReport.totalGrossAmount();
        dto.totalNetAmount = totalsReport.totalNetAmount();
        dto.totalNumberOfInvoices = totalsReport.totalNumberOfInvoices();
        dto.totalAverageGrossTotals = totalsReport.totalAverageGrossAmount();
        dto.establishments = totalsReport.totalsByEstablishment.stream()
                .map(this::toTotalsByEstablishmentDTO).collect(Collectors.toList());
        return dto;
    }

    private TotalsByEstablishmentDTO toTotalsByEstablishmentDTO(TotalsByEstablishment byEstablishment) {
        TotalsByEstablishmentDTO dto = new TotalsByEstablishmentDTO();
        dto.name = byEstablishment.establishmentName;
        dto.grossTotals = byEstablishment.grossTotals();
        dto.netTotals = byEstablishment.netTotals();
        dto.numberOfInvoices = byEstablishment.numberOfInvoices();
        dto.averageGrossTotals = byEstablishment.averageGrossTotals();
        return dto;
    }

    private HistoryReportDTO getReportDTO(String businessDayFrom, String businessDayTo,
                                          List<String> establishmentIds, GroupByPeriod groupByPeriod) {
        PeriodsReportFilters filters = new PeriodsReportFilters(businessDayFrom, businessDayTo, establishmentIds, groupByPeriod);
        ReportByPeriod report = reportService.generateReportByPeriods(filters);
        HistoryReportDTO dto = new HistoryReportAdapter().adapt(report);
        dto.filters = "?" + mapEstablishmentsIdsDTOFilter(establishmentIds)
                + "fromDay=" + businessDayFrom
                + "&toDay=" + businessDayTo
                + "&groupByPeriod=" + groupByPeriod;
        return dto;
    }
}
