import React from 'react';
import moment from 'moment';
import {shallow} from 'enzyme';
import ReportsForm from './ReportsForm';
import {reportsForm} from './commons/domIds';
import Checkbox from 'material-ui/Checkbox';
import RaisedButton from 'material-ui/RaisedButton';
import {
    simulateChangeInDateField,
    simulateCheckInCheckbox,
    simulateClickOnButton,
    simulateUnCheckInCheckbox
} from '../utils/testHelper';

describe('ReportsForm', () => {
    let establishment, establishments, page, handleForm;

    function createPage() {
        const router = {
            location: {
                search: '',
                query: {
                    toDay: '',
                    fromDay: '',
                    'establishments[]': []
                }
            }
        };
        page = shallow(
            <ReportsForm
                router={router}
                establishments={establishments}
                onFiltersChanged={handleForm}
            />);
    }

    beforeEach(() => {
        establishment = {
            id: 1,
            name: "foo"
        };
        establishments = [establishment];
        handleForm = jest.fn();
        createPage();
    });

    it('should contain the establishment in the checkboxes', () => {
        const checkboxes = page.find(Checkbox);
        expect(checkboxes.length).toEqual(1);

        const checkbox = checkboxes.first();
        expect(checkbox.props().value).toBe(establishment.id);
        expect(checkbox.props().label).toBe(establishment.name);
    });

    it('should add the establishmentId to the selected ones when its checkbox was checked', () => {
        const checkbox = page.find(Checkbox).first();

        simulateCheckInCheckbox(checkbox, establishment.id);

        expect(page.state().selectedEstablishments).toEqual([establishment.id]);
    });

    it('should remove the establishmentId to the selected ones when its checkbox was unchecked', () => {
        const checkbox = page.find(Checkbox).first();
        const selectedEstablishments = [establishment.id];

        page.setState({selectedEstablishments: selectedEstablishments});

        simulateUnCheckInCheckbox(checkbox, establishment.id);

        expect(page.state().selectedEstablishments).toEqual([]);
    });

    it('should call the request action when button is submitted with the query string', () => {
        const fromDate = moment();
        const toDate = moment();

        const checkbox = page.find(Checkbox).first();
        simulateCheckInCheckbox(checkbox, establishment.id);

        const fromDateInput = page.find(`#${reportsForm.fromDateInputId}`).first();
        simulateChangeInDateField(fromDateInput, fromDate);

        const toDateInput = page.find(`#${reportsForm.toDateInputId}`).first();
        simulateChangeInDateField(toDateInput, toDate);

        const button = page.find(RaisedButton).first();
        simulateClickOnButton(button);

        const queryString = `?establishments[]=${establishment.id}` +
            `&fromDay=${parseDate(fromDate)}` +
            `&toDay=${parseDate(toDate)}`;
        expect(handleForm).toBeCalledWith(queryString);
    });

    it('the submit button should be disabled if there is no establishment selected', () => {
        let button = page.find(RaisedButton).first();
        expect(button.props().disabled).toBe(true);

        const checkbox = page.find(Checkbox).first();
        simulateCheckInCheckbox(checkbox, establishment.id);

        button = page.update().find(RaisedButton).first();
        expect(button.props().disabled).toBe(false);
    });

    it('should set the minDate of the toDate input and change the date if the fromDate is lower than toDate', () => {
        const today = moment(new Date());
        const tomorrow = moment(new Date()).add(1, 'days');

        let toDateInput = page.find(`#${reportsForm.toDateInputId}`).first();
        simulateChangeInDateField(toDateInput, today);

        const fromDateInput = page.find(`#${reportsForm.fromDateInputId}`).first();
        simulateChangeInDateField(fromDateInput, tomorrow);

        toDateInput = page.update().find(`#${reportsForm.toDateInputId}`).first();
        expect(parseDate(moment(toDateInput.props().minDate))).toEqual(parseDate(tomorrow));
        expect(parseDate(moment(toDateInput.props().value))).toEqual(parseDate(tomorrow));
    });

    function parseDate(date) {
        return date.format('YYYY-MM-DD');
    }

});