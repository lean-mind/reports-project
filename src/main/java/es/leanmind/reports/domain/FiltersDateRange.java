package es.leanmind.reports.domain;

public class FiltersDateRange {
    public final String businessDayFrom;
    public final String businessDayTo;

    public FiltersDateRange(String businessDayFrom, String businessDayTo) {
        this.businessDayFrom = businessDayFrom;
        this.businessDayTo = businessDayTo;
    }
}
