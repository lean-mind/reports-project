/* eslint-disable import/default */
import React from 'react';
import './styles/style.css';
import 'bootstrap/dist/css/bootstrap.css';
import './styles/responsive-table.scss';
import routes from './routes';
import {render} from 'react-dom';
import {Provider} from 'react-redux';
import configureStore from './store/configureStore';
import {browserHistory, Router} from 'react-router';
import {syncHistoryWithStore} from 'react-router-redux';
import injectTapEventPlugin from 'react-tap-event-plugin';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

const store = configureStore();

const history = syncHistoryWithStore(browserHistory, store);

injectTapEventPlugin();

render(
    <Provider store={store}>
        <MuiThemeProvider>
            <Router history={history} routes={routes}/>
        </MuiThemeProvider>
    </Provider>, document.getElementById('app')
);
