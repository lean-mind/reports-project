import React from 'react';
import {Route} from 'react-router';
import App from './components/App';
import AppLayout from './components/AppLayout';
import LoginPage from './components/LoginPage';
import HomePage from './components/HomePage';
import NotFoundPage from './components/NotFoundPage';
import {HistoryReportPage, TotalsReportPage, HourlyReportPage, GroupedHoursReportPage} from './factory';
import * as paths from './routePaths';

export default (
    <Route component={App}>
        <Route path={paths.LOGIN} component={LoginPage}/>
        <Route path={paths.INDEX} component={AppLayout}>
            <Route path={paths.HOME} component={HomePage}/>
            <Route path={paths.REPORTS_TOTALS} component={TotalsReportPage}/>
            <Route path={paths.REPORTS_PERIOD} component={HourlyReportPage}/>
            <Route path={paths.REPORTS_GROUPED_HOURS} component={GroupedHoursReportPage}/>
            <Route path={paths.REPORTS_HISTORY} component={HistoryReportPage}/>
        </Route>
        <Route path="/*" component={NotFoundPage}/>
    </Route>
);
