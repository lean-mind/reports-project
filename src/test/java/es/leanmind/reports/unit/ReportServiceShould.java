package es.leanmind.reports.unit;

import es.leanmind.reports.domain.*;
import es.leanmind.reports.helpers.InvoicesHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReportServiceShould {

    private InvoiceRepository invoiceRepository;
    private ReportService reportService;
    private final int e1Id = 1;
    private final int e2Id = 2;
    private final String e1Name = "establishmentOne";
    private final String e2Name = "establishmentTwo";

    @Before
    public void setUp() throws Exception {
        invoiceRepository = mock(InvoiceRepository.class);
        reportService = new ReportService(invoiceRepository);
        InvoicesHelper.irrelevantInvoiceId = 0;
    }

    @Test
    public void create_a_totals_report() {
        ReportFilters filters = new ReportFilters(InvoicesHelper.october2, InvoicesHelper.october3, asList(e1Name, e2Name));
        StoredInvoice a = InvoicesHelper.aStoredInvoice(e1Id, InvoicesHelper.october2, InvoicesHelper.withAnyTime(InvoicesHelper.october2), "5.50", "5.00");
        StoredInvoice b = InvoicesHelper.aStoredInvoice(e2Id, InvoicesHelper.october2, InvoicesHelper.withAnyTime(InvoicesHelper.october2), "5.50", "5.00");
        StoredInvoice c = InvoicesHelper.aStoredInvoice(e2Id, InvoicesHelper.october3, InvoicesHelper.withAnyTime(InvoicesHelper.october3), "5.50", "5.00");

        when(invoiceRepository.retrieveStoredInvoices(filters))
                .thenReturn(InvoicesHelper.invoicesByEstablishment()
                            .with(e1Id, a)
                            .with(e2Id, b, c)
                            .areGiven());

        TotalsReport report = reportService.generateReport(filters);

        assertThat(report.totalNetAmount()).isEqualTo("15.00");
        assertThat(report.totalNumberOfInvoices()).isEqualTo(3);
        assertThat(report.totalGrossAmount()).isEqualTo("16.50");
        assertThat(report.totalAverageGrossAmount()).isEqualTo("5.50");

        assertThat(report.totalsByEstablishmentAt(0).numberOfInvoices).isEqualTo(1);
        assertThat(report.totalsByEstablishmentAt(0).grossTotals).isEqualTo("5.50");
        assertThat(report.totalsByEstablishmentAt(0).netTotals).isEqualTo("5.00");
        assertThat(report.totalsByEstablishmentAt(0).averageGrossTotals()).isEqualTo("5.50");

        assertThat(report.totalsByEstablishmentAt(1).numberOfInvoices).isEqualTo(2);
        assertThat(report.totalsByEstablishmentAt(1).grossTotals).isEqualTo("11.00");
        assertThat(report.totalsByEstablishmentAt(1).netTotals).isEqualTo("10.00");
        assertThat(report.totalsByEstablishmentAt(1).averageGrossTotals()).isEqualTo("5.50");
    }

    @Test
    public void create_a_totals_by_month_report() {
        List<String> establishments = asList(e1Name, e2Name);
        PeriodsReportFilters filters = new PeriodsReportFilters(InvoicesHelper.january1, InvoicesHelper.april30_2016, establishments,
                GroupByPeriod.Month);
        StoredInvoice e1January_1 = InvoicesHelper.aStoredInvoice(
                e1Id, InvoicesHelper.january2, InvoicesHelper.withAnyTime(InvoicesHelper.january2), "10", "8");
        StoredInvoice e1January_2 = InvoicesHelper.aStoredInvoice(
                e1Id, InvoicesHelper.january3, InvoicesHelper.withAnyTime(InvoicesHelper.january3), "20", "10");
        StoredInvoice e1February_1 = InvoicesHelper.aStoredInvoice(
                e1Id, InvoicesHelper.february1, InvoicesHelper.withAnyTime(InvoicesHelper.february1), "20", "10");
        StoredInvoice e2March_1 = InvoicesHelper.aStoredInvoice(
                e2Id, InvoicesHelper.march2, InvoicesHelper.withAnyTime(InvoicesHelper.march2), "10", "8");
        StoredInvoice e2March_2 = InvoicesHelper.aStoredInvoice(
                e2Id, InvoicesHelper.march3, InvoicesHelper.withAnyTime(InvoicesHelper.march3), "20", "10");
        StoredInvoice e2April_1 = InvoicesHelper.aStoredInvoice(
                e2Id, InvoicesHelper.april1_2016, InvoicesHelper.withAnyTime(InvoicesHelper.april1_2016), "20", "10");

        when(invoiceRepository.retrieveStoredInvoices(filters))
                .thenReturn(InvoicesHelper.invoicesByEstablishment()
                            .with(e1Id, e1January_1, e1January_2, e1February_1)
                            .with(e2Id, e2March_1, e2March_2, e2April_1)
                            .areGiven());

        ReportByPeriod report = reportService.generateReportByPeriods(filters);

        List<EstablishmentPeriodData> e1Data = report.byEstablishment(e1Id);
        assertThat(e1Data.size()).isEqualTo(4);
        assertThat(e1Data.get(0).grossTotals()).isEqualTo("30");
        assertThat(e1Data.get(0).numberOfInvoices()).isEqualTo(2);
        assertThat(e1Data.get(1).grossTotals()).isEqualTo("20");
        assertThat(e1Data.get(1).numberOfInvoices()).isEqualTo(1);
        assertThat(e1Data.get(2).grossTotals()).isEqualTo("0");
        assertThat(e1Data.get(2).numberOfInvoices()).isEqualTo(0);

        List<EstablishmentPeriodData> e2Data = report.byEstablishment(e2Id);
        assertThat(e2Data.size()).isEqualTo(4);
        assertThat(e2Data.get(0).grossTotals()).isEqualTo("0");
        assertThat(e2Data.get(0).numberOfInvoices()).isEqualTo(0);
        assertThat(e2Data.get(1).grossTotals()).isEqualTo("0");
        assertThat(e2Data.get(1).numberOfInvoices()).isEqualTo(0);
        assertThat(e2Data.get(2).grossTotals()).isEqualTo("30");
        assertThat(e2Data.get(2).numberOfInvoices()).isEqualTo(2);
        assertThat(e2Data.get(3).grossTotals()).isEqualTo("20");
        assertThat(e2Data.get(3).numberOfInvoices()).isEqualTo(1);
    }

    @Test
    public void indexes_months_starting_from_zero() {
        List<String> establishments = asList(e1Name);
        PeriodsReportFilters filters = new PeriodsReportFilters(InvoicesHelper.march1_2016, InvoicesHelper.may30, establishments,
                GroupByPeriod.Month);

        when(invoiceRepository.retrieveStoredInvoices(filters))
                .thenReturn(InvoicesHelper.invoicesByEstablishment().with(
                        e1Id,
                        InvoicesHelper.aStoredInvoice(e1Id, InvoicesHelper.march1_2016, InvoicesHelper.withAnyTime(InvoicesHelper.march1_2016), "10"),
                        InvoicesHelper.aStoredInvoice(e1Id, InvoicesHelper.monday_may2_2016, InvoicesHelper.withAnyTime(InvoicesHelper.monday_may2_2016), "10")).areGiven());

        ReportByPeriod report = reportService.generateReportByPeriods(filters);

        List<EstablishmentPeriodData> e1Data = report.byEstablishment(e1Id);
        assertThat(e1Data.size()).isEqualTo(3);
        assertThat(e1Data.get(0).grossTotals()).isEqualTo("10");
        assertThat(e1Data.get(0).numberOfInvoices()).isEqualTo(1);
        assertThat(e1Data.get(1).grossTotals()).isEqualTo("0");
        assertThat(e1Data.get(1).numberOfInvoices()).isEqualTo(0);
        assertThat(e1Data.get(2).grossTotals()).isEqualTo("10");
        assertThat(e1Data.get(2).numberOfInvoices()).isEqualTo(1);
    }

    @Test
    public void indexes_weeks_starting_from_zero() {
        List<String> establishments = asList(e1Name);
        PeriodsReportFilters filters = new PeriodsReportFilters(InvoicesHelper.march1_2016, InvoicesHelper.march22, establishments, GroupByPeriod.Weeks);
        String grossAmount = "10";
        when(invoiceRepository.retrieveStoredInvoices(filters))
                .thenReturn(InvoicesHelper.invoicesByEstablishment().with(
                        e1Id,
                        InvoicesHelper.aStoredInvoice(e1Id, InvoicesHelper.march1_2016, InvoicesHelper.withAnyTime(InvoicesHelper.march1_2016), grossAmount),
                        InvoicesHelper.aStoredInvoice(e1Id, InvoicesHelper.march22, InvoicesHelper.march22+ InvoicesHelper.beforeMidnight, grossAmount)
                ).areGiven());

        ReportByPeriod report = reportService.generateReportByPeriods(filters);

        List<EstablishmentPeriodData> e1Data = report.byEstablishment(e1Id);
        assertThat(e1Data.size()).isEqualTo(4);
        assertThat(e1Data.get(0).grossTotals()).isEqualTo(grossAmount);
        assertThat(e1Data.get(0).numberOfInvoices()).isEqualTo(1);
        assertThat(e1Data.get(1).grossTotals()).isEqualTo("0");
        assertThat(e1Data.get(1).numberOfInvoices()).isEqualTo(0);
        assertThat(e1Data.get(2).grossTotals()).isEqualTo("0");
        assertThat(e1Data.get(2).numberOfInvoices()).isEqualTo(0);
        assertThat(e1Data.get(3).grossTotals()).isEqualTo(grossAmount);
        assertThat(e1Data.get(3).numberOfInvoices()).isEqualTo(1);
    }

    @Test
    public void group_invoices_by_hours() {
        List<String> establishments = asList(e1Name);
        PeriodsReportFilters filters =
                new PeriodsReportFilters(InvoicesHelper.march1_2016, InvoicesHelper.march2_2016, establishments, GroupByPeriod.DaysWithHours);
        when(invoiceRepository.retrieveStoredInvoices(filters))
                .thenReturn(InvoicesHelper.invoicesByEstablishment().with(
                        e1Id,
                        InvoicesHelper.aStoredInvoice(e1Id, InvoicesHelper.march1_2016,
                                InvoicesHelper.withTime(InvoicesHelper.march1_2016, "06", "20"), "10"),
                        InvoicesHelper.aStoredInvoice(e1Id, InvoicesHelper.march1_2016,
                                InvoicesHelper.withTime(InvoicesHelper.march1_2016, "06", "50"), "10"),
                        InvoicesHelper.aStoredInvoice(e1Id, InvoicesHelper.march1_2016,
                                InvoicesHelper.withTime(InvoicesHelper.march1_2016, "07", "10"), "5")
                ).areGiven());

        ReportByPeriod report = reportService.generateReportByPeriods(filters);

        List<EstablishmentPeriodData> e1Data = report.byEstablishment(e1Id);
        assertThat(e1Data.size()).isEqualTo(48);
        assertThat(ReportByPeriod.openingTime).isEqualTo(5);
        assertThat(e1Data.get(0).grossTotals()).isEqualTo("0");
        assertThat(e1Data.get(0).numberOfInvoices()).isEqualTo(0);
        assertThat(e1Data.get(1).grossTotals()).isEqualTo("20");
        assertThat(e1Data.get(1).numberOfInvoices()).isEqualTo(2);
        assertThat(e1Data.get(2).grossTotals()).isEqualTo("5");
        assertThat(e1Data.get(2).numberOfInvoices()).isEqualTo(1);
        assertThat(e1Data.get(3).grossTotals()).isEqualTo("0");
        assertThat(e1Data.get(3).numberOfInvoices()).isEqualTo(0);
    }

    @Test
    public void group_invoices_by_hours_and_days() {
        List<String> establishments = asList(e1Name);
        PeriodsReportFilters filters =
                new PeriodsReportFilters(InvoicesHelper.march1_2016, InvoicesHelper.march2_2016, establishments, GroupByPeriod.DaysWithHours);
        when(invoiceRepository.retrieveStoredInvoices(filters))
                .thenReturn(InvoicesHelper.invoicesByEstablishment().with(
                        e1Id,
                        InvoicesHelper.aStoredInvoice(e1Id, InvoicesHelper.march1_2016,
                                InvoicesHelper.withTime(InvoicesHelper.march1_2016, "06", "20"), "10"),
                        InvoicesHelper.aStoredInvoice(e1Id, InvoicesHelper.march2_2016,
                                InvoicesHelper.withTime(InvoicesHelper.march2_2016, "07", "10"), "5")
                ).areGiven());

        ReportByPeriod report = reportService.generateReportByPeriods(filters);

        List<EstablishmentPeriodData> e1Data = report.byEstablishment(e1Id);
        assertThat(e1Data.size()).isEqualTo(48);
        assertThat(e1Data.get(26).grossTotals()).isEqualTo("5");
        assertThat(e1Data.get(26).numberOfInvoices()).isEqualTo(1);
    }
}
