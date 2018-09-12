package es.leanmind.reports.integration;

import es.leanmind.reports.controller.api.ReportsController;
import es.leanmind.reports.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static es.leanmind.reports.helpers.InvoicesHelper.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ReportsController.class, secure = false)
public class ApiReportsControllerShould {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportServiceStub;

    @Test
    public void retrieve_establishments_parsed_to_json() throws Exception {
        List<TotalsByEstablishment> totalsByEstablishments =
                asList(aTotalByEstablishment("fooEstablishment", 1, "29.50", "28.50"),
                        aTotalByEstablishment("barEstablishment", 2, "32.50", "30.50"));
        TotalsReport totalsReport = new TotalsReport(totalsByEstablishments);
        given(reportServiceStub.generateReport(any())).willReturn(totalsReport);

        String expectedReport =
        "{" +
            "\"totalGrossAmount\":\"" + totalsReport.totalGrossAmount() + "\"," +
            "\"totalNetAmount\":\""   + totalsReport.totalNetAmount() + "\"," +
            "\"totalNumberOfInvoices\":" + totalsReport.totalNumberOfInvoices() + "," +
            "\"totalAverageGrossTotals\":\"" + totalsReport.totalAverageGrossAmount() + "\"," +
            "\"filters\":\"?establishments[]=1&fromDay=2017-05-08&toDay=2017-05-08\"," +
            "\"establishments\":" +
               "[{\"name\":\"fooEstablishment\",\"grossTotals\":29.50, \"netTotals\":28.50, \"numberOfInvoices\":1, \"averageGrossTotals\":29.50}," +
               " {\"name\":\"barEstablishment\",\"grossTotals\":32.50, \"netTotals\":30.50, \"numberOfInvoices\":2, \"averageGrossTotals\":16.25}]" +
        "}";

        this.mockMvc.perform(get("/api/reports/json/totals")
                .param("fromDay", "2017-05-08")
                .param("toDay", "2017-05-08")
                .param("establishments[]", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedReport));
    }

    @Test
    public void retrieve_history_report_json() throws Exception {
        PeriodsReportFilters filters = new PeriodsReportFilters(
                april1_2016, october3, asList("1"), GroupByPeriod.Month);
        ReportByPeriod report = new ReportByPeriod(invoicesByEstablishment()
                .with(1,
                        aStoredInvoice(1, april1_2016, "5"),
                        aStoredInvoice(1, may30,  "3"))
                .areGiven(), filters);
        given(reportServiceStub.generateReportByPeriods(any())).willReturn(report);

        String expectedJson = "{" +
                                "\"periodNames\":[\"ABRIL\",\"MAYO\",\"JUNIO\",\"JULIO\",\"AGOSTO\",\"SEPTIEMBRE\",\"OCTUBRE\"]," +
                                "\"establishments\":[{" +
                                                        "\"name\":\"establishment_1\"," +
                                                        "\"grossTotals\":[\"5\",\"3\",\"0\",\"0\",\"0\",\"0\",\"0\"]" +
                                                    "}]," +
                                "\"filters\":\"?establishments[]=1&fromDay="+ april1_2016 + "&toDay=" + october3 + "&groupByPeriod=Month\"" +
                              "}";

        this.mockMvc.perform(get("/api/reports/json/history")
                .param("fromDay", april1_2016)
                .param("toDay", october3)
                .param("establishments[]", "1")
                .param("groupByPeriod", "Month"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }


    private TotalsByEstablishment aTotalByEstablishment(String name, Integer numberOfInvoices, String grossTotals, String netTotals) {
        return new TotalsByEstablishment(name, numberOfInvoices, new BigDecimal(grossTotals), new BigDecimal(netTotals));
    }

}
