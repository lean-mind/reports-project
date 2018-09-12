import React from 'react';
import {shallow} from 'enzyme';
import {TotalsReportPage} from './TotalsReportPage';
import * as apiRoutes from '../apiRoutes';

describe('Totals report page, shallow tests', () => {
    let establishments, page, report, filters, router;

    function createPage() {
        page = shallow(
            <TotalsReportPage
                establishments={establishments}
                notifierActions={{}}
                notifications={{}}
                router={router}
                actions={{}}
                report={report}
            />);
    }

    beforeEach(() => {
        filters = '?whatever';
        establishments = [{
            id: 1,
            name: "foo"
        }];
        router = {location: {search: filters}};
        report = {
            totalGrossAmount: 101,
            totalNetAmount: 52,
            totalNumberOfInvoices: 3,
            establishments: [
                {name: 'foo', grossTotals: 22, netTotals: 11, numberOfInvoices: 1},
                {name: 'bar', grossTotals: 32, netTotals: 28, numberOfInvoices: 2}
            ]
        };
        createPage();
    });

    it('should contain the summary', () => {
        const byEstablishment = page.find('#totals-summary').first().html();
        expect(byEstablishment).toContain(report.totalGrossAmount);
        expect(byEstablishment).toContain(report.totalNetAmount);
        expect(byEstablishment).toContain(report.totalNumberOfInvoices);
    });

    it('does not render report if there are no filters in url', () => {
        router = {location: {search: ""}};
        createPage();

        expect(page.find('#totals-summary').length).toBe(0);
    });

    it('should contain the report by establishment', () => {
        const byEstablishment = page.find('.summary-by-establishment').first().html();
        expect(byEstablishment).toContain(report.establishments[0].name);
        expect(byEstablishment).toContain(report.establishments[1].name);
        expect(byEstablishment).toContain(report.establishments[0].grossTotals);
        expect(byEstablishment).toContain(report.establishments[1].grossTotals);
        expect(byEstablishment).toContain(report.establishments[0].netTotals);
        expect(byEstablishment).toContain(report.establishments[1].netTotals);
    });

    it('should contain download links', () => {
        const downloads = page.find('#download-buttons').first().html();
        expect(downloads).toContain('href="' + apiRoutes.REPORTS_TOTALS_SPREADSHEET + filters + '"');
        expect(downloads).toContain('href="' + apiRoutes.REPORTS_TOTALS_PDF + filters + '"');
    });
});