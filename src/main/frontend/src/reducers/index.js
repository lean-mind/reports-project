import {combineReducers} from 'redux';
import {routerReducer} from 'react-router-redux';
import {reportsReducer} from './reportsReducer';
import {establishmentsReducer} from './establishmentsReducer';
import {notificationsReducer} from './notificationsReducer';
import * as types from '../actions/types';

const appReducer = combineReducers({
    routing: routerReducer,
    establishments: establishmentsReducer,
    reports: reportsReducer,
    notifications: notificationsReducer
});

const rootReducer = (state, action) => {
    if (action.type === types.DO_LOGOUT) {
        state = undefined;
    }
    return appReducer(state, action);
};

export default rootReducer;
