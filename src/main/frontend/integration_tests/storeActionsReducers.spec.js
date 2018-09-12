/* eslint-env node, mocha, jest */

import configureStore from '../src/store/configureStore';
import * as reportsActions from '../src/actions/reportsActions';
import * as actionTypes from '../src/actions/types';
import {expectStoreToContainAtSomePoint} from '../src/utils/testHelper';

describe('the connection of the store with actions and reducers', () => {
    let store, props, serverApi;

    beforeEach(() => {
        props = {};
        serverApi = {};
        store = configureStore(props);
    });

    describe('totals report', () => {
        it('updates store with totals report when is new', (done) => {
            let report = {totalGrossAmount: 100};
            serverApi.getTotalsReport = () => Promise.resolve(report);
            let filters = '?establishments[]=1&fromDay=2017-04-07&toDay=2017-05-13';
            store.dispatch(reportsActions.showTotalsReport(serverApi, filters));

            expectStoreToContainAtSomePoint(store, done,
                (state) => state.reports.totalsReport, report);
        });

        it('updates store with totals report when has changed query', () => {
            store.dispatch({
                type: actionTypes.REPORTS_TOTALS,
                report: {filters:'?foo', totalGrossAmount:100}
            });

            store.dispatch({
                type: actionTypes.REPORTS_TOTALS,
                report: {filters:'?bar', totalGrossAmount:100}
            });

            expect(store.getState().reports.totalsReport.filters).toEqual('?bar');
        });
    });

});
