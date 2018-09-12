import {reportService} from './services';


describe('report service', () => {
    it('requests a different report when filters change', () => {
        const state = {
            latestReportFilters: '/?foo'
        };
        const next = {
            location: {
                search: '/?whatever'
            }
        };
        expect(reportService().shouldRequestReport(next, state)).toBeTruthy();
    });

    it('requests a report when there is no report', () => {
        const state = {
            latestReportFilters: null
        };
        const next = {
            location: {
                search: '/?whatever'
            }
        };
        expect(reportService().shouldRequestReport(next, state)).toBeTruthy();
    });

    it('does not request report when is same filter', () => {
        const state = {
            latestReportFilters: '/?w'
        };
        const next = {
            location: {
                search: '/?w'
            }
        };
        expect(reportService().shouldRequestReport(next, state)).toBeFalsy();
    });
});
