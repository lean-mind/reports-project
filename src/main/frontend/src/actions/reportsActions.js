import * as types from './types';

export function showTotalsReport(serverApi, filters) {
    return (dispatch) => {
        return serverApi.getTotalsReport(filters).then((report) => {
            return dispatch({
                type: types.REPORTS_TOTALS,
                report: report
            });
        });
    };
}

export function showHourlyReport(serverApi, filters) {
    return (dispatch) => {
        return serverApi.getHourlyReport(filters).then((report) => {
            return dispatch({
                type: types.REPORTS_HOURLY,
                report: report
            });
        });
    };
}

export function showGroupedHoursReport(serverApi, filters) {
    return (dispatch) => {
        return serverApi.getGroupedHoursReport(filters).then((report) => {
            return dispatch({
                type: types.REPORTS_GROUPED_HOURS,
                report: report
            });
        });
    };
}

export function showTotalsHistoryReport(serverApi, filters) {
    return (dispatch) => {
        return serverApi.getTotalsHistory(filters).then((report) => {
            return dispatch({
                type: types.REPORTS_HISTORY_TOTALS,
                report: report
            });
        });
    };
}

export function loadEstablishments(serverApi) {
    return (dispatch) => {
        return serverApi.loadEstablishments().then((establishments) => {
            return dispatch({
                type: types.LOAD_ESTABLISHMENTS,
                establishments: establishments
            });
        });
    };
}

