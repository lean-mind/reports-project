import React, {PropTypes} from 'react';
import moment from 'moment';
import {reportsForm} from './commons/domIds';
import IntlPolyfill from 'intl';
import { Col, Grid, Row } from 'react-bootstrap';
import { Checkbox, DatePicker, RaisedButton } from 'material-ui';
import styles from '../styles/report-page';
import Radium from 'radium';

class ReportsForm extends React.Component {

    constructor(props, context) {
        super(props, context);
        this.handleForm = this.handleForm.bind(this);
        this.dateTimeFormat = this.dateTimeFormat.bind(this);
        this.datePickerFieldFormat = this.datePickerFieldFormat.bind(this);
        this.onToDateChange = this.onToDateChange.bind(this);
        this.onFromDateChange = this.onFromDateChange.bind(this);
        this.onEstablishmentSelection = this.onEstablishmentSelection.bind(this);
        this.mapEstablishmentsToCheckboxes = this.mapEstablishmentsToCheckboxes.bind(this);

        const query = this.props.router.location.query;
        const createFromDayFromQuery = () => {
            const [year, month, day] = query.fromDay.split('-');
            return new Date(year, month - 1, day);
        };

        const createToDayFromQuery = () => {
            const [year, month, day] = query.toDay.split('-');
            return new Date(year, month - 1, day);
        };

        const filters = this.props.router.location.search;
        let selectedEstablishments = !filters ? [] : query['establishments[]'];
        if (typeof(selectedEstablishments) === 'string') {
            // WTF. Esto solo pasa cuando la url directamente tiene querystring
            // no lo reproduzco con tests ahora.
            selectedEstablishments = [selectedEstablishments];
        }
        this.state = {
            selectedEstablishments: selectedEstablishments,
            fromDate: !filters ? new Date() : createFromDayFromQuery(),
            toDate: !filters ? new Date() : createToDayFromQuery()
        };
    }

    dateTimeFormat() {
        require('intl/locale-data/jsonp/es');
        require('intl/locale-data/jsonp/es-ES');
        return IntlPolyfill.DateTimeFormat;
    }

    datePickerFieldFormat(date) {
        moment.locale('es-ES');
        return moment(date).format('LL');
    }

    mapEstablishmentsToCheckboxes() {
        const {establishments} = this.props;
        return establishments.map((establishment) => {
            return (
                <Col sm={4} lg={3} key={establishment.id}>
                    <Checkbox label={establishment.name}
                              value={establishment.id}
                              className="establishment-checkbox"
                              onCheck={this.onEstablishmentSelection}
                              style={styles.establishmentCheckbox}
                              inputStyle={styles.establishmentCheckbox.input}
                              iconStyle={styles.establishmentCheckbox.icon}
                              labelStyle={styles.establishmentCheckbox.label}
                              checked={this.state.selectedEstablishments.includes(`${establishment.id}`)}/>
                </Col>
            );
        });
    }

    onEstablishmentSelection(event, hasBeenSelected) {
        let newState;
        const establishmentId = event.target.value;
        if (hasBeenSelected) {
            newState = {
                selectedEstablishments: [...this.state.selectedEstablishments, establishmentId]
            };
        } else {
            newState = {
                selectedEstablishments: this.state.selectedEstablishments.filter(id => id != establishmentId)
            };
        }
        this.setState(Object.assign({}, this.state, newState));
    }

    onFromDateChange(event, newDate) {
        const actualToDate = this.state.toDate;
        const date = !event ? newDate : event.target.value;
        const newState = {
            fromDate: date,
            toDate: actualToDate < date ? date : actualToDate
        };
        this.setState(Object.assign({}, this.state, newState));
    }

    onToDateChange(event, newDate) {
        const date = !event ? newDate : event.target.value;
        this.setState(Object.assign({}, this.state, {toDate: date}));
    }

    handleForm() {
        function appendEstablishments(establishments) {
            return establishments.reduce((acc, nextValue) => {
                return `${acc}establishments[]=${nextValue}&`;
            }, '');
        }

        function appendDates(fromDate, toDate) {
            return `fromDay=${formatDate(fromDate)}&toDay=${formatDate(toDate)}`;
        }

        function formatDate(date) {
            return moment(date).format('YYYY-MM-DD');
        }

        const {fromDate, toDate, selectedEstablishments} = this.state;
        let url = '?';
        url += appendEstablishments(selectedEstablishments);
        url += appendDates(fromDate, toDate);
        this.props.onFiltersChanged(url);
    }

    render() {
        return (
            <Grid>
                <Row>
                    {this.mapEstablishmentsToCheckboxes()}
                </Row>
                <Row>
                    <Col sm={5} smOffset={1} md={4} mdOffset={2} lg={3} lgOffset={3}>
                        <DatePicker autoOk={true}
                                    locale="es-ES"
                                    hintText="Desde"
                                    cancelLabel="Cancelar"
                                    floatingLabelText="Desde"
                                    container="inline"
                                    floatingLabelStyle={styles.datePicker.floatingLabel}
                                    formatDate={this.datePickerFieldFormat}
                                    textFieldStyle={styles.datePicker.textField}
                                    value={this.state.fromDate}
                                    id={reportsForm.fromDateInputId}
                                    onChange={this.onFromDateChange}
                                    DateTimeFormat={this.dateTimeFormat()}/>
                    </Col>
                    <Col sm={5} md={4} lg={3}>
                        <DatePicker autoOk={true}
                                    locale="es-ES"
                                    hintText="Hasta"
                                    cancelLabel="Cancelar"
                                    floatingLabelText="Hasta"
                                    container="inline"
                                    floatingLabelStyle={styles.datePicker.floatingLabel}
                                    formatDate={this.datePickerFieldFormat}
                                    textFieldStyle={styles.datePicker.textField}
                                    value={this.state.toDate}
                                    minDate={this.state.fromDate}
                                    id={reportsForm.toDateInputId}
                                    onChange={this.onToDateChange}
                                    DateTimeFormat={this.dateTimeFormat()}/>
                    </Col>
                </Row>
                {this.props.children &&
                <Row>
                    <Col sm={5} smOffset={1} md={4} mdOffset={2} lg={3} lgOffset={3}>
                        {this.props.children}
                    </Col>
                </Row>
                }
                <Row>
                    <Col sm={10} smOffset={1} md={8} mdOffset={2} lg={6} lgOffset={3}>
                        <RaisedButton label="Generar Informe"
                                    primary={true}
                                    id="filter-button"
                                    name="submitReport"
                                    style={styles.raisedButton}
                                    labelStyle={styles.raisedButton.label}
                                    onTouchTap={this.handleForm}
                                    disabled={this.state.selectedEstablishments.length === 0}/>
                    </Col>
                </Row>
            </Grid>
        );
    }
}

ReportsForm.propTypes = {
    router: PropTypes.object.isRequired,
    onFiltersChanged: PropTypes.func.isRequired,
    establishments: PropTypes.array.isRequired,
    children: PropTypes.object
};

export default Radium(ReportsForm);
