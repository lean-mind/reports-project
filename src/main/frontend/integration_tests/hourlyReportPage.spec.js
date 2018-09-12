import React from 'react';
import {mount} from 'enzyme';
import {Provider} from 'react-redux';
import {createHourlyReportPage} from '../src/factory';
import configureStore from '../src/store/configureStore';
import injectTapEventPlugin from 'react-tap-event-plugin';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {createNotifierActions} from '../src/factory';
import { StyleRoot } from 'radium';
import {clearAllStores} from '../src/utils/testHelper';

describe("The hourly report page", () => {
    const timeout = 10;
    let store, page, props, stubApi, spyNotifier, stubRouter, establishments;
    const emptyDummyFunc = () => {};

    function mountPage(actions = null, props = null) {
        const connectRouterToPage = (state) => {
            return {
                routing: state.routing
            };
        };
        const Page = createHourlyReportPage(
                            stubApi, spyNotifier, actions, connectRouterToPage);
        page = mount(
            <Provider store={store}>
                <MuiThemeProvider>
                    <StyleRoot>
                        <Page router={stubRouter} {...props}/>
                    </StyleRoot>
                </MuiThemeProvider>
            </Provider>);
    }

    function mountPageSimulatingThatLocationIs(newLocation) {
        let simulateWhatReactRouterReallyDoesWithLocation = {
            location: {search: newLocation}
        };
        mountPage(null, simulateWhatReactRouterReallyDoesWithLocation);
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
        establishments = [{id: 1, name: 'foo'},{id: 2, name: 'bar'}];
        props = {
            reports : {
                hourlyReport: {
                    periodNames: [
                        '2016-04-01 - 5h a 6h',
                        '2016-04-01 - 6h a 7h',
                        '2016-04-01 - 7h a 8h'
                    ],
                    establishments: [
                        {   name: 'foo',
                            grossTotals: [10, 20, 30],
                            netTotals:   [9,  19, 29],
                            numberOfInvoices: [1,1,1]
                        },
                        {   name: 'bar',
                            grossTotals: [40, 50, 60],
                            netTotals:   [39, 49, 59],
                            numberOfInvoices: [1,1,1]
                        },
                    ]
                }
            },
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
            getHourlyReport :    () => Promise.resolve({}),
            loadEstablishments : () => Promise.resolve([])
        };
        store = configureStore(props);
    }, timeout);

    afterEach(() => {
        // avoid race conditions in async tests:
        clearRequestWaiter();
        clearAllStores();
    });

    describe('the interaction with the GUI and the store', () => {

          it('renders hourly report when available', () => {
            simulateQueryString("/?someFilters");

            mountPageSimulatingThatLocationIs(currentQueryString());

            let hourlyReport = props.reports.hourlyReport;

            expect(page.html()).toContain(hourlyReport.establishments[0].grossTotals[0]);
            expect(page.html()).toContain(hourlyReport.establishments[0].netTotals[0]);
            expect(page.html()).toContain(hourlyReport.establishments[1].grossTotals[0]);
            expect(page.html()).toContain(hourlyReport.establishments[1].netTotals[0]);
        });
    });
});