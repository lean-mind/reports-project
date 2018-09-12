export function simulateChangeInDateField(dateField, propertyValue) {
    dateField.simulate('change', {target: {value: propertyValue}});
}

export function simulateCheckInCheckbox(checkbox, propertyValue) {
    checkbox.simulate('check', {target: {value: propertyValue}}, true);
}

export function simulateUnCheckInCheckbox(checkbox, propertyValue) {
    checkbox.simulate('check', {target: {value: propertyValue}}, false);
}

export function simulateClickOnButton(button) {
    button.simulate('touchTap');
}

export function simulateChangeInField(guiField, propertyName, propertyValue) {
    guiField.simulate("change", {
        target: {
            name: propertyName,
            value: propertyValue,
            validity: {valid: true}
        },
        preventDefault: () => {
        }
    });
}

export function simulateInvalidInput(guiField, propertyName, propertyValue) {
    guiField.simulate("blur", {
        target: {
            name: propertyName,
            value: propertyValue,
            validity: {valid: false}
        },
        preventDefault: () => {
        }
    });
}

export function simulateClick(guiField, propertyName) {
    guiField.simulate("click", {
        target: {
            name: propertyName
        },
        preventDefault: () => {
        }
    });
}

export function inputById(id) {
    return 'input[id="' + id + '"]';
}

export function inputByClass(cls) {
    return '.' + cls;
}

let subscribers = [];
export function clearAllStores(){
    subscribers.forEach(s => s()); // redux unsubscribe
    subscribers = [];
}
export function expectStoreToContainAtSomePoint(store, done, fieldAccessor, expected) {
    subscribers.push(store.subscribe(() => {
        try {
            let actual = fieldAccessor(store.getState());
            expect(actual).toEqual(expected);
            done();
        }
        catch (err) {
            done.fail(err);
        }
    }));
}

export function spyDisplay() {
    return {
        dialog: {
            alert: () => {
                return {
                    close: () => {
                    }
                };
            }
        }
    };
}

export function eventuallyExpect(expectation, done) {
    return function () {
        try {
            expectation();
            if (done) { done();}
        }
        catch (err) {
            if (done) {done.fail(err);}
        }
    };
}

export function dispatchLocationChange(store){
    // This will break if future versions of react-router
    // change the type of the action.
    store.dispatch({
        type: '@@router/LOCATION_CHANGE',
        payload: {}
    });
}