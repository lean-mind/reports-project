import {notificationsReducer} from './notificationsReducer';
import * as actions from '../actions/notifierActions';

describe('notifications reducer', () => {
    let notifications;

    it('do not change state when showing spinner twice', () => {
        notifications = {
            dialogState: {
                isOpen: false
            },
            spinnerState: {
                text: 'Cargando datos...',
                isOpen: true,
                slowOperationsInProgress: 1
            }
        };
        const result = notificationsReducer(notifications, actions.showSpinner());

        expect(notifications).toBe(result);
    });

    it('change state when showing spinner twice', () => {
        notifications = {
            dialogState: {
                isOpen: false
            },
            spinnerState: {
                text: 'Cargando datos...',
                isOpen: false,
                slowOperationsInProgress: 1
            }
        };
        const result = notificationsReducer(notifications, actions.showSpinner());

        expect(result.spinnerState).toMatchObject({
            text: notifications.spinnerState.text,
            isOpen: true,
            slowOperationsInProgress: 1
        });
    });

    it('do not open dialog right after a slow operation starts', () => {
        notifications = {
            dialogState: {
                isOpen: false
            },
            spinnerState: {
                text: 'Cargando datos...',
                isOpen: false,
                slowOperationsInProgress: 0
            }
        };
        const result = notificationsReducer(notifications, actions.startSlowOperation());

        expect(result.spinnerState).toMatchObject({
            text: notifications.spinnerState.text,
            isOpen: false,
            slowOperationsInProgress: 1
        });
    });

    it('changes state when slow operation finishes', () => {
        notifications = {
            dialogState: {
                isOpen: false
            },
            spinnerState: {
                text: 'Cargando datos...',
                isOpen: true,
                slowOperationsInProgress: 1
            }
        };
        const result = notificationsReducer(notifications, actions.slowOperationFinished());

        expect(result.spinnerState).toMatchObject({
            text: notifications.spinnerState.text,
            isOpen: false,
            slowOperationsInProgress: 0
        });
    });

    it('do not open spinner if slow operation has finished already', () => {
        notifications = {
            dialogState: {
                isOpen: false
            },
            spinnerState: {
                text: 'Cargando datos...',
                isOpen: false,
                slowOperationsInProgress: 0
            }
        };
        const result = notificationsReducer(notifications, actions.showSpinner());

        expect(result.spinnerState).toMatchObject({
            text: notifications.spinnerState.text,
            isOpen: false,
            slowOperationsInProgress: 0
        });
    });

    it('handles multiple slow operations happening at the same time', () => {
        notifications = {
            dialogState: {
                isOpen: false
            },
            spinnerState: {
                text: 'Cargando datos...',
                isOpen: false,
                slowOperationsInProgress: 0
            }
        };
        let result = notificationsReducer(notifications, actions.startSlowOperation());
        result = notificationsReducer(result, actions.startSlowOperation());
        result = notificationsReducer(result, actions.slowOperationFinished());

        expect(result.spinnerState).toMatchObject({
            text: notifications.spinnerState.text,
            isOpen: false,
            slowOperationsInProgress: 1
        });
    });

    it('do not show the same dialog twice', () => {
        const msg = 'testing';
        notifications = {
            dialogState: {
                text: msg,
                isOpen: true
            },
            spinnerState: {
                text: 'Cargando datos...',
                isOpen: false,
                slowOperationsInProgress: 0
            }
        };
        const result = notificationsReducer(notifications, actions.alert(msg));

        expect(notifications).toBe(result);
    });
});