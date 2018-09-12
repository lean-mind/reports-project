import * as actionTypes from '../actions/types';

export function reportsReducer(reports, action) {
    if (actionTypes.REPORTS_TOTALS === action.type) {
        if (!reports.totalsReport || reports.totalsReport.filters !== action.report.filters) {
            return Object.assign({}, reports, {totalsReport: action.report});
        }
    }
    if (actionTypes.REPORTS_HOURLY === action.type) {
        if (!reports.hourlyReport || reports.hourlyReport.filters !== action.report.filters) {
            return Object.assign({}, reports, {hourlyReport: action.report});
        }
    }
    if (actionTypes.REPORTS_GROUPED_HOURS === action.type) {
        if (!reports.groupedHoursReport || reports.groupedHoursReport.filters !== action.report.filters) {
            return Object.assign({}, reports, {groupedHoursReport: action.report});
        }
    }
    if (actionTypes.REPORTS_HISTORY_TOTALS === action.type) {
        if (!reports.historyReport || reports.historyReport.filters !== action.report.filters) {
            return Object.assign({}, reports, {historyReport: action.report});
        }
    }
    return reports || {};
}