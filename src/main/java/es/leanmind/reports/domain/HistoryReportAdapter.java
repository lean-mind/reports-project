package es.leanmind.reports.domain;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class HistoryReportAdapter {

    public HistoryReportDTO adapt(ReportByPeriod report) {
        HistoryReportDTO dto = new HistoryReportDTO();
        dto.periodNames = report.getPeriodNames();
        dto.establishments = report.establishmentsIds()
                                   .stream()
                                   .map(id -> toEstablishmentHistoryDTO(id, report))
                                   .collect(toList());
        return dto;
    }

    private EstablishmentHistoryDTO toEstablishmentHistoryDTO(Integer id, ReportByPeriod report) {
        EstablishmentHistoryDTO dto = new EstablishmentHistoryDTO();
        dto.name = report.establishmentName(id);
        List<EstablishmentPeriodData> establishmentPeriodData = report.byEstablishment(id);
        dto.grossTotals = establishmentPeriodData
                .stream()
                .map(e -> e.grossTotals().toString())
                .toArray(String[]::new);
        dto.netTotals = establishmentPeriodData
                .stream()
                .map(e -> e.netTotals().toString())
                .toArray(String[]::new);
        dto.numberOfInvoices = establishmentPeriodData
                .stream()
                .map(e -> e.numberOfInvoices().toString())
                .toArray(String[]::new);

        return dto;
    }
}
