import React, {PropTypes} from 'react';
import {Line as LineChart} from 'react-chartjs-2';
import {RadioButton, RadioButtonGroup} from 'material-ui/RadioButton';
import ReportPage from './ReportPage';
import 'chartjs-plugin-zoom';

export class HistoryReportPage extends ReportPage {

    constructor(props, context) {
        super(props, context);
        this.renderChart = this.renderChart.bind(this);
        this.changeGroupByPeriod = this.changeGroupByPeriod.bind(this);
        this.state = {
            groupByPeriod: 'Month'
        };
    }

    requestReport(filters) {
        return this.props.actions.showHistoryReport(filters);
    }

    filtersToUrl(filters){
        return `${this.props.router.location.pathname}${filters}&groupByPeriod=${this.state.groupByPeriod}`;
    }

    renderChart() {
        let {report} = this.props;
        if (!report || !report.establishments) return;
        const randomColor = (a) => {
            const r = Math.floor(Math.random() * 255);
            const g = Math.floor(Math.random() * 255);
            const b = Math.floor(Math.random() * 255);
            return `rgba(${r}, ${g}, ${b}, ${a})`;
        };

        const mapEstablishmentsToChartDatasets = (establishments = []) => {
            return establishments.map((establishment) => {
                const color = randomColor('0.4');
                return {
                    label: establishment.name,
                    fill: false,
                    lineTension: 0.1,
                    backgroundColor: color,
                    borderColor: color,
                    borderCapStyle: 'butt',
                    borderDash: [],
                    borderDashOffset: 0.0,
                    borderJoinStyle: 'miter',
                    pointBorderColor: "rgba(75,192,192,1)",
                    pointBackgroundColor: "#fff",
                    pointBorderWidth: 1,
                    pointHoverRadius: 5,
                    pointHoverBackgroundColor: "rgba(75,192,192,1)",
                    pointHoverBorderColor: "rgba(220,220,220,1)",
                    pointHoverBorderWidth: 2,
                    pointRadius: 1,
                    pointHitRadius: 10,
                    spanGaps: false,
                    data: establishment.grossTotals
                };
            });
        };

        const chartData = {
            labels: report.periodNames,
            datasets: mapEstablishmentsToChartDatasets(report.establishments)
        };

        const chartOptions = {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            },
            title: {
                display: true,
                text: 'Historico de ventas'
            },
            pan: {
                enabled: true,
                mode: 'y'
            },
            zoom: {
                enabled: true,
                mode: 'xy',
            }
        };
        return <LineChart data={chartData} redraw options={chartOptions}/>;
    }

    changeGroupByPeriod(event) {
        this.setState(Object.assign({}, this.state, {groupByPeriod: event.target.value}));
    }

    renderFormChildren(){
        return (
        <RadioButtonGroup name="groupByPeriod"
                          onChange={this.changeGroupByPeriod}
                          defaultSelected={this.state.groupByPeriod}
                          style={{display:'flex'}}>
            <RadioButton value="Days"
                         label="Diario"
                         onChange=""/>
            <RadioButton value="Weeks"
                         label="Semanal"/>
            <RadioButton value="Month"
                         label="Mensual"/>
        </RadioButtonGroup>);
    }

    renderReport(){
        return (
            <div>
                <div className="totals-report" style={{textAlign: 'center', marginTop: '50px'}}>
                    <div className="totals-history"
                         style={{
                             width:'50%', height: '10%', textAlign: 'center',
                             marginLeft: '25%'
                         }}>
                        {this.renderChart()}
                    </div>
                </div>
            </div>
        );
    }
}

HistoryReportPage.propTypes = {
    report: PropTypes.object,
    establishments: PropTypes.array,
    router: PropTypes.object.isRequired,
    actions: PropTypes.object.isRequired,
    notifierActions: PropTypes.object.isRequired
};
