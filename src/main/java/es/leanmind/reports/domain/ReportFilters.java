package es.leanmind.reports.domain;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class ReportFilters {

    private final String businessDayFrom;
    private final String businessDayTo;
    private final List<String> establishmentIds;

    public ReportFilters(String businessDayFrom, String businessDayTo, List<String> establishmentIds) {
        this.businessDayFrom = businessDayFrom;
        this.businessDayTo = businessDayTo;
        this.establishmentIds = establishmentIds;
    }

    public boolean haveDateRangeContaining(String aBusinessDay) {
        LocalDate businessDay = LocalDate.parse(aBusinessDay);
        return businessDay.isEqual(businessDayFrom())
                || businessDay.isEqual(businessDayTo())
                || businessDay.isAfter(businessDayFrom())
                && businessDay.isBefore(businessDayTo());
    }

    public LocalDate businessDayFrom() {
        return LocalDate.parse(businessDayFrom);
    }

    public LocalDate businessDayTo() {
        return LocalDate.parse(businessDayTo);
    }

    public List<String> establishments() {
        return Collections.unmodifiableList(establishmentIds);
    }
}
