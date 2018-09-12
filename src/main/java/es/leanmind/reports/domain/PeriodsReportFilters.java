package es.leanmind.reports.domain;

import java.util.List;

public class PeriodsReportFilters extends ReportFilters {
    private final GroupByPeriod period;

    public PeriodsReportFilters(String fromDay, String toDay, List<String> establishments, GroupByPeriod period) {
        super(fromDay, toDay, establishments);
        this.period = period;
    }

    public GroupByPeriod groupByPeriod() {
        return period;
    }
}
