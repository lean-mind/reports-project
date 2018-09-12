import * as actionTypes from '../actions/types';

function getSpinnerStateCopy(notifications) {
    return Object.assign({}, notifications.spinnerState);
}
function notificationsCopyWith(notifications, spinnerState){
    return Object.assign({}, notifications, {spinnerState});
}

export function notificationsReducer(notifications, action) {
    if (action.type === actionTypes.START_SLOW_OPERATION){
        let spinnerState = getSpinnerStateCopy(notifications);
        spinnerState.slowOperationsInProgress = notifications.spinnerState.slowOperationsInProgress + 1;
        return notificationsCopyWith(notifications, spinnerState);
    }
    if (action.type === actionTypes.SLOW_OPERATION_FINISHED){
        let spinnerState = getSpinnerStateCopy(notifications);
        let currentOperationsInProgress = notifications.spinnerState.slowOperationsInProgress -1;
        spinnerState.isOpen = notifications.spinnerState.isOpen && currentOperationsInProgress !== 0;
        spinnerState.slowOperationsInProgress = currentOperationsInProgress;
        return notificationsCopyWith(notifications, spinnerState);
    }
    if (action.type === actionTypes.SHOW_SPINNER){
        if (notifications.spinnerState.slowOperationsInProgress > 0 && !notifications.spinnerState.isOpen) {
            let spinnerState = getSpinnerStateCopy(notifications);
            spinnerState.isOpen = true;
            return notificationsCopyWith(notifications, spinnerState);
        }
    }
    if (action.type === actionTypes.HIDE_SPINNER){
        if (notifications.spinnerState.isOpen === true) {
            let spinnerState = Object.assign({}, notifications.spinnerState);
            spinnerState.isOpen = false;
            return Object.assign({}, notifications, {spinnerState});
        }
    }
    if (action.type === actionTypes.SHOW_DIALOG){
        if (notifications.dialogState.text !== action.msg ||
            notifications.dialogState.isOpen === false) {
            return Object.assign({}, notifications, {
                dialogState: {
                    text: action.msg,
                    isOpen: true
                }
            });
        }
    }
    if (action.type === actionTypes.HIDE_DIALOG){
        if (notifications.dialogState.isOpen === true) {
            return Object.assign({}, notifications, {
                dialogState: {
                    text: notifications.dialogState.text,
                    isOpen: false
                }
            });
        }
    }
    return notifications || {
        dialogState: {
            isOpen: false
        },
        spinnerState: {
            text: 'Cargando datos...',
            isOpen: false,
            slowOperationStarted: false,
            slowOperationsInProgress: 0
        }
    };
}