import {connect} from 'react-redux';
import createServerApi from './serverApi';
import * as reportsActions from './actions/reportsActions';
import * as notifierActions from './actions/notifierActions';
import {TotalsReportPage as OriginalTotalReportPage} from './components/TotalsReportPage';
import {HistoryReportPage as OriginalHistoryReportPage} from './components/HistoryReportPage';
import {HourlyReportPage as OriginalHourlyReportPage} from './components/HourlyReportPage';
import Radium from 'radium';


export const createNotifierActions = (dispatch) => ({
    startSlowOperation: () => {
        dispatch(notifierActions.startSlowOperation());
        let timeToWaitBeforeShowingSpinner = 500;
        setTimeout(() => {
            dispatch(notifierActions.showSpinner());
        }, timeToWaitBeforeShowingSpinner);
    },
    slowOperationFinished: () => {
        dispatch(notifierActions.slowOperationFinished());
    },
    alert: (msg) => {
        dispatch(notifierActions.alert(msg));
    },
    hideDialog: () => {
        dispatch(notifierActions.hideDialog());
    }
});

export const createTotalsReportPage = (serverApi, notifier, actions, mapState) => {
    return connect(
        (state) => {
            let mapping = {
                report: state.reports.totalsReport,
                establishments: state.establishments,
                notifications: state.notifications
            };
            if (typeof(mapState) === 'function'){
                return Object.assign(mapping, mapState(state));
            }
            return mapping;
        },
        (dispatch) => ({
            notifierActions: notifier || createNotifierActions(dispatch),
            actions: actions || {
                showReport: (filters) => {
                    return dispatch(reportsActions.showTotalsReport(serverApi, filters));
                },
                loadEstablishments: () => {
                    return dispatch(reportsActions.loadEstablishments(serverApi));
                }
            }
        })
    )(Radium(OriginalTotalReportPage));
};

export const createHourlyReportPage = (serverApi, notifier, actions, mapState) => {
    return connect(
        (state) => {
            let mapping = {
                report: state.reports.hourlyReport,
                establishments: state.establishments,
                notifications: state.notifications
            };
            if (typeof(mapState) === 'function'){
                return Object.assign(mapping, mapState(state));
            }
            return mapping;
        },
        (dispatch) => ({
            notifierActions: notifier || createNotifierActions(dispatch),
            actions: actions || {
                showReport: (filters) => {
                    return dispatch(reportsActions.showHourlyReport(serverApi, filters));
                },
                loadEstablishments: () => {
                    return dispatch(reportsActions.loadEstablishments(serverApi));
                }
            }
        })
    )(Radium(OriginalHourlyReportPage));
};

export const createGroupedHoursReportPage = (serverApi, notifier, actions, mapState) => {
    return connect(
        (state) => {
            let mapping = {
                report: state.reports.groupedHoursReport,
                establishments: state.establishments,
                notifications: state.notifications
            };
            if (typeof(mapState) === 'function'){
                return Object.assign(mapping, mapState(state));
            }
            return mapping;
        },
        (dispatch) => ({
            notifierActions: notifier || createNotifierActions(dispatch),
            actions: actions || {
                showReport: (filters) => {
                    return dispatch(reportsActions.showGroupedHoursReport(serverApi, filters));
                },
                loadEstablishments: () => {
                    return dispatch(reportsActions.loadEstablishments(serverApi));
                }
            }
        })
    )(Radium(OriginalHourlyReportPage));
};

export const createHistoryReportPage = (serverApi, notifier, actions) => {
    return connect(
        (state) => ({
            report: state.reports.historyReport,
            establishments: state.establishments,
            notifications: state.notifications
        }),
        (dispatch) => ({
            notifierActions: notifier || createNotifierActions(dispatch),
            actions: actions || {
                showHistoryReport: (filters) => {
                    return dispatch(reportsActions.showTotalsHistoryReport(serverApi, filters));
                },
                loadEstablishments: () => {
                    return dispatch(reportsActions.loadEstablishments(serverApi));
                }
            }
        })
    )(Radium(OriginalHistoryReportPage));
};

export const TotalsReportPage = createTotalsReportPage(createServerApi());
export const HourlyReportPage = createHourlyReportPage(createServerApi());
export const HistoryReportPage = createHistoryReportPage(createServerApi());
export const GroupedHoursReportPage = createGroupedHoursReportPage(createServerApi());