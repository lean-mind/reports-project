package es.leanmind.reports.domain;

import java.util.List;

public class TotalsReportDTO {
    public String totalGrossAmount;
    public String totalNetAmount;
    public Integer totalNumberOfInvoices;
    public String filters;
    public List<TotalsByEstablishmentDTO> establishments;
    public String totalAverageGrossTotals;
}
