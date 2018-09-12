package es.leanmind.reports.unit;

import es.leanmind.reports.domain.*;
import es.leanmind.reports.helpers.InvoicesHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static es.leanmind.reports.domain.GroupByPeriod.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class HistoryReportAdapterShould {

    private HistoryReportAdapter adapter;

    @Before
    public void setUp() throws Exception {
        adapter = new HistoryReportAdapter();
    }

    @Test
    public void generate_dto_by_months_ready_for_chart_js() throws Exception {
        PeriodsReportFilters filters = new PeriodsReportFilters(
                InvoicesHelper.april1_2016, InvoicesHelper.october3, asList("1","2"), Month);
        ReportByPeriod report = new ReportByPeriod(InvoicesHelper.invoicesByEstablishment()
            .with(1,
                InvoicesHelper.aStoredInvoice(1, InvoicesHelper.april1_2016, "5"),
                InvoicesHelper.aStoredInvoice(1, InvoicesHelper.may30,  "3"))
            .with(2,
                InvoicesHelper.aStoredInvoice(2, InvoicesHelper.october2, "10"))
            .areGiven(), filters);
        HistoryReportAdapter adapter = this.adapter;

        HistoryReportDTO dto = adapter.adapt(report);
        assertThat(dto.periodNames)
                .containsExactly(
                        "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE");
        EstablishmentHistoryDTO e1Dto = new EstablishmentHistoryDTO();
        e1Dto.name = "establishment_1";
        e1Dto.grossTotals = new String[]{"5", "3", "0", "0", "0", "0", "0"};
        EstablishmentHistoryDTO e2Dto = new EstablishmentHistoryDTO();
        e2Dto.name = "establishment_2";
        e2Dto.grossTotals = new String[]{"0", "0", "0", "0", "0", "0", "10"};
        assertThat(dto.establishments).contains(e1Dto);
        assertThat(dto.establishments).contains(e2Dto);
    }

    @Test
    public void generate_dto_by_months_also_when_year_changes() throws Exception {
        PeriodsReportFilters filters = new PeriodsReportFilters(
                "2016-10-10", "2017-01-31", asList("1"), Month);
        ReportByPeriod report = new ReportByPeriod(InvoicesHelper.invoicesByEstablishment()
                .with(1,
                        InvoicesHelper.aStoredInvoice(1, "2016-10-10",  "3"))
                .areGiven(), filters);

        HistoryReportDTO dto = this.adapter.adapt(report);
        assertThat(dto.periodNames)
                .containsExactly(
                        "OCTUBRE", "NOVIEMBRE", "DICIEMBRE", "ENERO");
        EstablishmentHistoryDTO e1Dto = new EstablishmentHistoryDTO();
        e1Dto.name = "establishment_1";
        e1Dto.grossTotals = new String[]{"3", "0", "0", "0"};
    }

    @Test
    public void generate_dto_by_days_ready_for_chart_js() throws Exception {
        PeriodsReportFilters filters = new PeriodsReportFilters(
                InvoicesHelper.april1_2016, InvoicesHelper.april30_2016, asList("1"), Days);
        ReportByPeriod report = new ReportByPeriod(InvoicesHelper.invoicesByEstablishment()
                .with(1,
                        InvoicesHelper.aStoredInvoice(1, InvoicesHelper.april1_2016, "5"),
                        InvoicesHelper.aStoredInvoice(1, InvoicesHelper.april30_2016,  "3"))
                .areGiven(), filters);

        HistoryReportDTO dto = this.adapter.adapt(report);
        assertThat(dto.periodNames.size()).isEqualTo(30);
        assertThat(dto.periodNames.get(0)).isEqualTo(InvoicesHelper.april1_2016);
        assertThat(dto.periodNames.get(29)).isEqualTo(InvoicesHelper.april30_2016);
        assertThat(dto.establishments.get(0).grossTotals.length).isEqualTo(30);
        assertThat(dto.establishments.get(0).grossTotals[0]).isEqualTo("5");
        assertThat(dto.establishments.get(0).grossTotals[29]).isEqualTo("3");
    }

    @Test
    public void generate_hourly_report_dto() throws Exception {
        PeriodsReportFilters filters = new PeriodsReportFilters(
                InvoicesHelper.march1_2016, InvoicesHelper.march2_2016, asList("1"), DaysWithHours);
        ReportByPeriod report = new ReportByPeriod(InvoicesHelper.invoicesByEstablishment()
                .with(1,
                        InvoicesHelper.aStoredInvoice(1, InvoicesHelper.march1_2016,
                                InvoicesHelper.withTime(InvoicesHelper.march1_2016, "06", "00"),
                                "5", "4"),
                        InvoicesHelper.aStoredInvoice(1, InvoicesHelper.march1_2016,
                                InvoicesHelper.withTime(InvoicesHelper.march1_2016, "07", "00"),
                                "3", "2"))
                .areGiven(), filters);

        HistoryReportDTO dto = this.adapter.adapt(report);
        assertThat(dto.periodNames.size()).isEqualTo(48);
        assertThat(dto.periodNames.get(0)).isEqualTo(InvoicesHelper.march1_2016 + " - 5h a 6h");
        assertThat(dto.periodNames.get(1)).isEqualTo(InvoicesHelper.march1_2016 + " - 6h a 7h");
        assertThat(dto.periodNames.get(2)).isEqualTo(InvoicesHelper.march1_2016 + " - 7h a 8h");
        EstablishmentHistoryDTO establishmentHistoryDTO = dto.establishments.get(0);
        assertThat(establishmentHistoryDTO.grossTotals.length).isEqualTo(48);
        assertThat(establishmentHistoryDTO.grossTotals[1]).isEqualTo("5");
        assertThat(establishmentHistoryDTO.netTotals[1]).isEqualTo("4");
        assertThat(establishmentHistoryDTO.numberOfInvoices[1]).isEqualTo("1");
        assertThat(establishmentHistoryDTO.grossTotals[2]).isEqualTo("3");
        assertThat(establishmentHistoryDTO.netTotals[2]).isEqualTo("2");
    }

    @Test
    public void generate_grouped_hours_report_dto() throws Exception {
        PeriodsReportFilters filters = new PeriodsReportFilters(
                InvoicesHelper.march1_2016, InvoicesHelper.march2_2016, asList("1"), GroupedHours);
        ReportByPeriod report = new ReportByPeriod(InvoicesHelper.invoicesByEstablishment()
                .with(1,
                        InvoicesHelper.aStoredInvoice(1, InvoicesHelper.march1_2016,
                                InvoicesHelper.withTime(InvoicesHelper.march1_2016, "06", "00"),
                                "5", "4"),
                        InvoicesHelper.aStoredInvoice(1, InvoicesHelper.march2_2016,
                                InvoicesHelper.withTime(InvoicesHelper.march2_2016, "06", "30"),
                                "3", "2"))
                .areGiven(), filters);

        HistoryReportDTO dto = this.adapter.adapt(report);
        assertThat(dto.periodNames.size()).isEqualTo(24);
        assertThat(dto.periodNames.get(0)).isEqualTo("5h a 6h");
        assertThat(dto.periodNames.get(1)).isEqualTo("6h a 7h");
        EstablishmentHistoryDTO establishmentHistoryDTO = dto.establishments.get(0);
        assertThat(establishmentHistoryDTO.grossTotals.length).isEqualTo(24);
        assertThat(establishmentHistoryDTO.grossTotals[0]).isEqualTo("0");
        assertThat(establishmentHistoryDTO.netTotals[0]).isEqualTo("0");
        assertThat(establishmentHistoryDTO.numberOfInvoices[0]).isEqualTo("0");
        assertThat(establishmentHistoryDTO.grossTotals[1]).isEqualTo("8");
        assertThat(establishmentHistoryDTO.netTotals[1]).isEqualTo("6");
        assertThat(establishmentHistoryDTO.numberOfInvoices[1]).isEqualTo("2");
    }

    @Test
    public void generate_dto_by_days_including_next_month_ready_for_chart_js() throws Exception {
        PeriodsReportFilters filters = new PeriodsReportFilters(
                InvoicesHelper.march1_2016, InvoicesHelper.april30_2016, asList("1"), Days);
        ReportByPeriod report = new ReportByPeriod(InvoicesHelper.invoicesByEstablishment()
                .with(1,
                        InvoicesHelper.aStoredInvoice(1, InvoicesHelper.march1_2016,  "8.00"),
                        InvoicesHelper.aStoredInvoice(1, InvoicesHelper.april30_2016,  "5.00"))
                .areGiven(), filters);

        HistoryReportDTO dto = this.adapter.adapt(report);
        assertThat(dto.periodNames).hasSize(61);
        assertThat(dto.periodNames.get(0)).isEqualTo(InvoicesHelper.march1_2016);
        assertThat(dto.periodNames.get(60)).isEqualTo(InvoicesHelper.april30_2016);

        EstablishmentHistoryDTO firstEstablishment = dto.establishments.get(0);
        assertThat(firstEstablishment.grossTotals).hasSize(61)
                                                  .hasSameSizeAs(dto.periodNames);
        assertThat(firstEstablishment.grossTotals[0]).isEqualTo("8.00");
        assertThat(firstEstablishment.grossTotals[60]).isEqualTo("5.00");
    }

    @Test
    public void generate_dto_by_weeks_ready_for_chart_js() throws Exception {
        PeriodsReportFilters filters = new PeriodsReportFilters(
                InvoicesHelper.april1_2016, InvoicesHelper.april30_2016, asList("1"), Weeks);
        ReportByPeriod report = new ReportByPeriod(InvoicesHelper.invoicesByEstablishment()
                .with(1,
                        InvoicesHelper.aStoredInvoice(1, InvoicesHelper.april1_2016, "5.00"),
                        InvoicesHelper.aStoredInvoice(1, InvoicesHelper.april30_2016,  "3.00"))
                .areGiven(), filters);

        HistoryReportDTO dto = this.adapter.adapt(report);
        assertThat(dto.periodNames).hasSize(5)
                                   .containsExactly(String.format("%s - %s", InvoicesHelper.april1_2016, "2016-04-03"),
                                                    "2016-04-04 - 2016-04-10",
                                                    "2016-04-11 - 2016-04-17",
                                                    "2016-04-18 - 2016-04-24",
                                                    String.format("%s - %s", "2016-04-25", InvoicesHelper.april30_2016));

        EstablishmentHistoryDTO firstEstablishment = dto.establishments.get(0);
        assertThat(firstEstablishment.grossTotals).hasSize(5)
                                                  .hasSameSizeAs(dto.periodNames);
        assertThat(firstEstablishment.grossTotals[0]).isEqualTo("5.00");
        assertThat(firstEstablishment.grossTotals[4]).isEqualTo("3.00");
    }

    @Test
    public void generate_dto_by_weeks_including_next_month_ready_for_chart_js() throws Exception {
        PeriodsReportFilters filters = new PeriodsReportFilters(
                InvoicesHelper.sunday_april3_2016, InvoicesHelper.monday_may2_2016, asList("1"), Weeks);
        ReportByPeriod report = new ReportByPeriod(InvoicesHelper.invoicesByEstablishment()
                .with(1,
                        InvoicesHelper.aStoredInvoice(1, InvoicesHelper.sunday_april3_2016, "5.00"),
                        InvoicesHelper.aStoredInvoice(1, "2016-04-05", "6.00"),
                        InvoicesHelper.aStoredInvoice(1, "2016-04-12", "8.00"),
                        InvoicesHelper.aStoredInvoice(1, "2016-04-19", "10.00"),
                        InvoicesHelper.aStoredInvoice(1, "2016-04-26", "12.00"),
                        InvoicesHelper.aStoredInvoice(1, InvoicesHelper.monday_may2_2016,  "14.00"))
                .areGiven(), filters);

        HistoryReportDTO dto = this.adapter.adapt(report);
        assertThat(dto.periodNames).hasSize(6)
                                   .containsExactly(String.format("%s - %s", InvoicesHelper.sunday_april3_2016, "2016-04-03"),
                                                    "2016-04-04 - 2016-04-10",
                                                    "2016-04-11 - 2016-04-17",
                                                    "2016-04-18 - 2016-04-24",
                                                    "2016-04-25 - 2016-05-01",
                                                    String.format("%s - %s", InvoicesHelper.monday_may2_2016, InvoicesHelper.monday_may2_2016));

        EstablishmentHistoryDTO firstEstablishment = dto.establishments.get(0);
        assertThat(firstEstablishment.grossTotals).hasSize(6)
                                                  .hasSameSizeAs(dto.periodNames)
                                                  .isEqualTo(new String[]{"5.00", "6.00", "8.00", "10.00", "12.00", "14.00"});
    }

    @Test
    public void retrieve_the_report_when_the_first_months_day_is_greater_than_the_lasts() throws Exception {
        PeriodsReportFilters filters = new PeriodsReportFilters(InvoicesHelper.february2, InvoicesHelper.may1, new ArrayList<>(), Month);
        ReportByPeriod report = new ReportByPeriod(
                InvoicesHelper.invoicesByEstablishment().with(1, InvoicesHelper.aStoredInvoice(1, InvoicesHelper.may1, "5.00")).areGiven(),
                filters);

        HistoryReportDTO reportDTO = adapter.adapt(report);
        assertThat(reportDTO.periodNames).hasSize(4)
                                         .containsExactly("FEBRERO", "MARZO", "ABRIL", "MAYO");
        assertThat(reportDTO.establishments.get(0).grossTotals).hasSize(4)
            .containsExactlyElementsOf(Arrays.asList("0", "0", "0", "5.00"));
    }

    @Test
    public void retrieve_the_weekly_report_when_the_first_weeks_day_monday() throws Exception {
        String monday3 = "2017-04-03";
        String monday17 = "2017-04-17";
        PeriodsReportFilters filters = new PeriodsReportFilters(monday3, monday17, new ArrayList<>(), Weeks);
        ReportByPeriod report = new ReportByPeriod(
                InvoicesHelper.invoicesByEstablishment().with(1, InvoicesHelper.aStoredInvoice(1, monday3, "5.00")).areGiven(),
                filters);

        HistoryReportDTO reportDTO = adapter.adapt(report);
        assertThat(reportDTO.periodNames).hasSize(3)
                .containsExactly("2017-04-03 - 2017-04-09",
                                 "2017-04-10 - 2017-04-16",
                                 format("%s - %s", monday17, monday17));
        assertThat(reportDTO.establishments.get(0).grossTotals).hasSize(3)
                .containsExactlyElementsOf(asList("5.00", "0", "0"));
    }
}
