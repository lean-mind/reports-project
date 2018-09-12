import * as routePaths from "./routePaths";

function getMessage(obj) {
    if (!obj) return '';

    if (!obj.message) return '';

    let msg = obj.message;
    if (obj.additionalMessage){
        msg += '<br/>' + obj.additionalMessage;
    }
    return msg;
}

function isNotAuthorized(error){
    return error.toString().indexOf('401') >= 0;
}

export function handleAsyncRequest(promise, notifier, router) {
    if (!promise || typeof(promise.then) !== 'function') {
        throw new Error('Invalid argument, expecting a Promise but was: ' + typeof(promise));
    }
    let handler = {id: null};
    notifier.startSlowOperation(handler);
    promise.then(
        (success) => {
            notifier.slowOperationFinished(handler);
            let message = getMessage(success);
            if (message) {
                notifier.alert(message);
            }
        },
        (error) => {
            /* eslint-disable */
            console.log(error); // useful to debug tests
            /* eslint-enable */
            notifier.slowOperationFinished(handler);
            if (!isNotAuthorized(error)) {
                let message = getMessage(error);
                notifier.alert(message || error);
            }
            else {
                router.push(routePaths.LOGIN);
            }
        });
}
