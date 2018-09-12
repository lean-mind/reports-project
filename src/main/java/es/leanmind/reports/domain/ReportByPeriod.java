package es.leanmind.reports.domain;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.*;
import static es.leanmind.reports.domain.GroupByPeriod.*;

public class ReportByPeriod {

    private final InvoicesByEstablishment invoices;
    private final PeriodsReportFilters filters;
    public  static int openingTime = 5; // 5am is the default opening time for establishments


    public ReportByPeriod(InvoicesByEstablishment invoices, PeriodsReportFilters filters) {
        this.invoices = invoices;
        this.filters = filters;
    }

    public Set<Integer> establishmentsIds() {
        return this.invoices.establishmentsIds();
    }

    public String establishmentName(Integer id) {
        return invoices.establishmentName(id);
    }

    public List<EstablishmentPeriodData> byEstablishment(int id) {
        EstablishmentPeriodData[] periodsData = initializePeriodsData(filters);
        for (StoredInvoice invoice : invoices.get(id)) {
            if (filters.haveDateRangeContaining(invoice.businessDay())) {
                int index = calculateIndex(invoice, filters);
                periodsData[index].addInvoice(invoice);
            }
        }
        return Arrays.asList(periodsData);
    }

    private EstablishmentPeriodData[] initializePeriodsData(PeriodsReportFilters filters) {
        EstablishmentPeriodData[] periodsData = new EstablishmentPeriodData[numberOfPeriods(filters)];
        for (int i = 0; i < periodsData.length; i++) {
            periodsData[i] = new EstablishmentPeriodData();
        }
        return periodsData;
    }

    private int numberOfPeriods(PeriodsReportFilters filters) {
        if (filters.groupByPeriod() == Month) {
            return (int) ChronoUnit.MONTHS.between(filters.businessDayFrom().with(firstDayOfMonth()), filters.businessDayTo().with(firstDayOfMonth())) + 1;
        }
        if (filters.groupByPeriod() == Weeks) {
            // WEEKS.between compares if passes exactly (weeks * 7) days between dates
            return (int) ChronoUnit.WEEKS.between(filters.businessDayFrom().with(previousOrSame(MONDAY)), filters.businessDayTo()) + 1;
        }
        if (filters.groupByPeriod() == Days) {
            return (int) daysBetween(filters.businessDayFrom(), filters.businessDayTo()) + 1;
        }
        if (filters.groupByPeriod() == DaysWithHours) {
            return ((int) daysBetween(filters.businessDayFrom(), filters.businessDayTo())) * 24 + 24;
        }
        if (filters.groupByPeriod() == GroupedHours) {
            return 24;
        }
        throw new NotImplementedException();
    }

    private int calculateIndex(StoredInvoice invoice, PeriodsReportFilters filters) {
        LocalDateTime invoiceDate = LocalDateTime.parse(invoice.date());
        if (filters.groupByPeriod() == Month) {
            return (int) ChronoUnit.MONTHS.between(filters.businessDayFrom().with(firstDayOfMonth()), invoiceDate.with(firstDayOfMonth()));
        }
        if (filters.groupByPeriod() == Weeks) {
            // WEEKS.between compares if passes exactly (weeks * 7) days between dates
            return (int) ChronoUnit.WEEKS.between(filters.businessDayFrom().with(previousOrSame(MONDAY)), invoiceDate);
        }
        if (filters.groupByPeriod() == Days) {
            return (int) ChronoUnit.DAYS.between(filters.businessDayFrom(), invoiceDate);
        }
        if (filters.groupByPeriod() == DaysWithHours) {
            int dayIndex = (int) ChronoUnit.DAYS.between(filters.businessDayFrom(), invoiceDate);
            int slotsInAday = 24;
            return dayIndex * slotsInAday + calculateHourSlot(invoiceDate);
        }
        if (filters.groupByPeriod() == GroupedHours){
            return calculateHourSlot(invoiceDate);
        }
        throw new NotImplementedException();
    }

    private int calculateHourSlot(LocalDateTime invoiceDate) {
        int hourSlot = invoiceDate.getHour() - openingTime;
        if (hourSlot < 0) {
            hourSlot += 24;
        }
        return hourSlot;
    }

    public List<String> getPeriodNames() {
        List<String> names = new ArrayList<>();
        if (filters.groupByPeriod() == Month) {
            LocalDate from = filters.businessDayFrom().with(firstDayOfMonth());
            while (!from.isAfter(filters.businessDayTo())) {
                names.add(SpanishMonth.names[from.getMonthValue() - 1]);
                from = from.plusMonths(1);
            }
            return names;
        }
        if (filters.groupByPeriod() == Days) {
            LocalDate from = filters.businessDayFrom();
            while (!from.isAfter(filters.businessDayTo())) {
                names.add(from.toString());
                from = from.plusDays(1);
            }
            return names;
        }
        if (filters.groupByPeriod() == Weeks) {
            LocalDate to = filters.businessDayTo();
            LocalDate from = filters.businessDayFrom();
            while (!from.isAfter(to)) {
                LocalDate endOfWeek = calculateEndOfWeek(from, to);
                names.add(from + " - " + endOfWeek);
                from = from.plusDays(calculateDifferenceBetween(from, endOfWeek));
            }
            return names;
        }
        if (filters.groupByPeriod() == DaysWithHours) {
            LocalDate from = filters.businessDayFrom();
            while (!from.isAfter(filters.businessDayTo())) {
                for (int i = openingTime; i < 24 + openingTime; i++){
                    String day = from.toString();
                    if (i >= 24){
                        day = from.plusDays(1).toString();
                    }
                    names.add(day + " - " + (i % 24) + "h a " + ((i+1) % 24) + "h");
                }
                from = from.plusDays(1);
            }
            return names;
        }
        if (filters.groupByPeriod() == GroupedHours) {
            for (int i = openingTime; i < 24 + openingTime; i++){
                names.add((i % 24) + "h a " + ((i+1) % 24) + "h");
            }
            return names;
        }
        return new ArrayList<>();
    }

    private long calculateDifferenceBetween(LocalDate from, LocalDate endOfWeek) {
        return daysBetween(from, endOfWeek) + 1;
    }

    private long daysBetween(LocalDate from, LocalDate endOfWeek) {
        return ChronoUnit.DAYS.between(from, endOfWeek);
    }

    private LocalDate calculateEndOfWeek(LocalDate from, LocalDate to) {
        LocalDate nextSunday = from.with(nextOrSame(SUNDAY));
        return to.isBefore(nextSunday) ? to : nextSunday;
    }
}
