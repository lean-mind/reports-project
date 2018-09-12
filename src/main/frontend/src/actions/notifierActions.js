import * as types from './types';

export function showSpinner() {
    return {
       type: types.SHOW_SPINNER,
       isOpen: true
    };
}

export function hideSpinner() {
    return {
        type: types.HIDE_SPINNER,
        isOpen: false
    };
}

export function alert(msg) {
    return {
        type: types.SHOW_DIALOG,
        msg: msg
    };
}

export function hideDialog() {
    return {
        type: types.HIDE_DIALOG
    };
}

export function startSlowOperation() {
    return {
        type: types.START_SLOW_OPERATION
    };
}

export function slowOperationFinished() {
    return {
        type: types.SLOW_OPERATION_FINISHED
    };
}