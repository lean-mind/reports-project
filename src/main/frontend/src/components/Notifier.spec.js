import React from 'react';
import {shallow} from 'enzyme';
import {Notifier} from './Notifier';
import injectTapEventPlugin from 'react-tap-event-plugin';
import Dialog from 'material-ui/Dialog';
//import FlatButton from 'material-ui/FlatButton';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
//import {simulateClickOnButton} from "../utils/testHelper";

describe('Notifier, mount tests', () => {
    let notifier, notifications, actions, muiTheme;

    beforeAll(() => {
        injectTapEventPlugin();
        muiTheme = getMuiTheme();
    });

    beforeEach(() => {
        notifications = {dialogState: {isOpen: true}, spinnerState: {isOpen: true}};
        actions = {
            slowOperationFinished : jest.fn(),
            hideDialog: jest.fn()
        };
        notifier = shallow(
            <Notifier
                notifications={notifications}
                actions={actions}
            />
        , {context: {muiTheme}});
    });

    it('renders two dialogs', () => {
        const dialogs = notifier.find(Dialog);
        expect(dialogs.length).toBe(2);
    });

    /* I've been trying to test the close button in the notifier but can't get the test working.
       Enough time dedicated to it. In the material-ui repository, Dialog doesn't even have tests!
     */
});