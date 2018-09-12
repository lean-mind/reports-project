import React, {PropTypes} from 'react';
import {handleAsyncRequest} from '../ux';
import ReportsForm from './ReportsForm';
import {Notifier} from './Notifier';
import {reportService} from '../services';

export default class ReportPage extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.handleFiltersChange = this.handleFiltersChange.bind(this);
        this.requestReport = this.requestReport.bind(this);
        this.renderReport = this.renderReport.bind(this);
        this.state = {
            latestReportFilters: null
        };
    }

    componentDidMount() {
        if (this.props.establishments.length != 0) return;

        // if establishments are loaded successfully
        // they will populate the store and so
        // componentWillReceiveProps will be executed
        // right after
        handleAsyncRequest(
            this.props.actions.loadEstablishments(),
            this.props.notifierActions,
            this.props.router);
    }

    // Executes every time the routers changes the URL
    // as well as when the observed properties change
    componentWillReceiveProps(nextProps){
        let shouldRequestReport = reportService().shouldRequestReport(nextProps, this.state);
        if (shouldRequestReport) {
            const filters = nextProps.location.search;
            this.setState({
                latestReportFilters: filters
            }, () => {
                handleAsyncRequest(
                    this.requestReport(filters),
                    this.props.notifierActions,
                    this.props.router);
            });
        }
    }

    requestReport(filters){
        return this.props.actions.showReport(filters);
    }

    handleFiltersChange(filters){
        const fullUrl = this.filtersToUrl(filters);
        this.props.router.push(fullUrl);
    }

    filtersToUrl(filters){
        return this.props.router.location.pathname + filters;
    }

    renderReport(){
        return null;
    }

    renderFormChildren(){
        return null;
    }

    render() {
        return (
            <div className="totals-report-page">
                <Notifier
                    notifications={this.props.notifications}
                    actions={this.props.notifierActions}
                />
                <ReportsForm
                    router={this.props.router}
                    establishments={this.props.establishments}
                    onFiltersChanged={this.handleFiltersChange}
                >
                    {this.renderFormChildren()}
                </ReportsForm>
                {this.props.router.location.search &&
                 this.renderReport()}
            </div>
        );
    }
}

ReportPage.propTypes = {
    router: PropTypes.object.isRequired,
    establishments: PropTypes.array,
    actions: PropTypes.object.isRequired,
    notifierActions: PropTypes.object.isRequired,
    notifications: PropTypes.object.isRequired,
    report: PropTypes.object
};