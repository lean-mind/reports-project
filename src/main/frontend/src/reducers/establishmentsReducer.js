import * as actionTypes from '../actions/types';

export function establishmentsReducer(establishments, action) {
    if (action.type === actionTypes.LOAD_ESTABLISHMENTS){
        return action.establishments;
    }
    return establishments || [];
}