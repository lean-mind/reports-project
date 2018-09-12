import React from 'react';
import {mount} from 'enzyme';
import {Provider} from 'react-redux';
import Checkbox from 'material-ui/Checkbox';
import {createTotalsReportPage} from '../src/factory';
import configureStore from '../src/store/configureStore';
import injectTapEventPlugin from 'react-tap-event-plugin';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {createNotifierActions} from '../src/factory';
import { StyleRoot } from 'radium';
import {eventuallyExpect, expectStoreToContainAtSomePoint, clearAllStores,
    dispatchLocationChange} from '../src/utils/testHelper';

describe("The totals report page", () => {
    const timeout = 10;
    let store, page, props, stubApi, spyNotifier, stubRouter, establishments;
    const emptyDummyFunc = () => {};
    const dateFrom = '2016-05-05';
    const dateTo = '2016-06-06';

    function mountTotalsReportPage(actions = null, props = null) {
        const connectRouterToPage = (state) => {
            return {
                routing: state.routing
            };
        };
        const TotalsPage = createTotalsReportPage(
                            stubApi, spyNotifier, actions, connectRouterToPage);
        page = mount(
            <Provider store={store}>
                <MuiThemeProvider>
                    <StyleRoot>
                        <TotalsPage router={stubRouter} {...props}/>
                    </StyleRoot>
                </MuiThemeProvider>
            </Provider>);
    }

    function mountPageSimulatingThatLocationIs(newLocation) {
        let simulateWhatReactRouterReallyDoesWithLocation = {
            location: {search: newLocation}
        };
        mountTotalsReportPage(null, simulateWhatReactRouterReallyDoesWithLocation);
    }

    function onceArequestToServerHasBeenProcessed(expectation, done) {
        spyNotifier.slowOperationFinished = eventuallyExpect(expectation, done);
    }

    function clearRequestWaiter(){
        spyNotifier.slowOperationFinished = emptyDummyFunc;
    }

    function setupStoreWith(props) {
        store = configureStore(Object.assign({
            reports: {},
            establishments: []
        }, props));
    }

    function simulateQueryString(queryString){
        stubRouter.location.search = queryString;
    }

    function currentQueryString(){
        return stubRouter.location.search;
    }

    beforeAll(() => {
        injectTapEventPlugin();
    });

    beforeEach(() => {
        establishments = [];
        props = {
            reports: {},
            establishments: establishments
        };
        stubRouter = {
            location: {
                search: '',
                query: {
                    toDay: '',
                    fromDay: '',
                    'establishments[]': []
                }
            },
            push: () => {
            }
        };
        let dummyDispatch = () => {};
        spyNotifier = createNotifierActions(dummyDispatch);
        stubApi = {
            getTotalsReport :    () => Promise.resolve({}),
            loadEstablishments : () => Promise.resolve([])
        };
        store = configureStore(props);
    }, timeout);

    afterEach(() => {
        // avoid race conditions in async tests:
        clearRequestWaiter();
        clearAllStores();
    });

    describe('when there are filters set in the query string', () => {
        const establishment = {
            id: 1,
            name: 'FooBar'
        };
        beforeEach(() => {
            simulateQueryString(
                `&establishments[]=${establishment.id}&dateFrom=${dateFrom}&dateTo=${dateTo}`);
        });

        it('requests the report to the server when url changes', (done) => {
            let filtersSent = null;
            stubApi.getTotalsReport = (filters) => {
                filtersSent = filters;
                return Promise.resolve({});
            };

            mountPageSimulatingThatLocationIs(currentQueryString());

            onceArequestToServerHasBeenProcessed(() => {
                    expect(filtersSent).toEqual(currentQueryString());
            }, done);
        });
    });

    describe('the interaction with the GUI and the store', () => {

        it('renders establishments in the form when it has establishments in the store', () => {
            const establishment = {id: 1, name: 'foo'};
            const establishments = [establishment];
            setupStoreWith({establishments: establishments});
            mountTotalsReportPage();

            const checkboxes = page.find(Checkbox);
            expect(checkboxes.length).toEqual(establishments.length);

            const checkbox = checkboxes.first();
            expect(checkbox.props().value).toBe(establishment.id);
            expect(checkbox.props().label).toBe(establishment.name);
        });

        it('renders totals report when available', () => {
            simulateQueryString("/?someFilters");
            let reports = {
                totalsReport: {
                    totalGrossAmount: '192.3',
                    totalNetAmount: '161',
                    totalNumberOfInvoices: 7
                }
            };
            setupStoreWith({
                establishments: [{id: 1, name: 'foo'}],
                reports: reports
            });
            mountPageSimulatingThatLocationIs(currentQueryString());

            expect(page.html()).toContain(reports.totalsReport.totalGrossAmount);
            expect(page.html()).toContain(reports.totalsReport.totalNetAmount);
            expect(page.html()).toContain(reports.totalsReport.totalNumberOfInvoices);
        });
    });

    describe('the interaction with te store', () => {
        it('requests establishments to load the store', (done) => {
            simulateQueryString("");
            const retrievedEstablishments = [
                {id: 1, name: 'foo'},
                {id: 2, name: 'bar'}
            ];
            stubApi.loadEstablishments = () => Promise.resolve(retrievedEstablishments);

            mountTotalsReportPage();

            expectStoreToContainAtSomePoint(store, done,
                (state) => state.establishments, retrievedEstablishments);
        }, timeout);

        it('requests report to populate the store', (done) => {
            simulateQueryString('?someParameters');
            let report = {totalGrossAmount: 100, filters: currentQueryString()};
            setupStoreWith({establishments : [{id: 1, name: 'foo'}]});
            stubApi.getTotalsReport = () => {
                return Promise.resolve(report);
            };
            mountPageSimulatingThatLocationIs(currentQueryString());

            dispatchLocationChange(store);

            expectStoreToContainAtSomePoint(store, done,
                 (state) => state.reports.totalsReport, report);
        }, timeout);


        it('should not requests establishments when they are in the store', () => {
            setupStoreWith({establishments : [{id: 1, name: 'foo'}]});
            stubApi.loadEstablishments = jest.fn();

            mountTotalsReportPage();

            expect(stubApi.loadEstablishments).not.toHaveBeenCalled();
        });
    });
});

