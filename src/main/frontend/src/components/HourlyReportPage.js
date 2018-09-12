import React, {PropTypes} from 'react';
//import * as apiRoutes from '../apiRoutes';
import { Grid as TempGrid, Row, Col } from 'react-bootstrap';
import styles from '../styles/totals-report';
//import { FaFileExcelO, FaFilePdfO } from 'react-icons/lib/fa';
import Radium from 'radium';
import ReportPage from './ReportPage';

const Grid = Radium(TempGrid);

export class HourlyReportPage extends ReportPage {

    requestReport(filters){
        return this.props.actions.showReport(filters);
    }

    filtersToUrl(filters){
        return `${this.props.router.location.pathname}${filters}&groupByPeriod=Hours`;
    }

    renderReport(){
        if (!this.props.report) return null;
        let {report} = this.props;
        return (
            <Grid style={styles.root}>
                <Row id="download-buttons">
                    <Col xs={6} sm={3} smOffset={6} lg={2} lgOffset={8}/>
                    <Col xs={6} sm={3} lg={2}/>
                </Row>
                <table id="totals-summary" className="responsive-table summary-by-establishment">
                    <tbody>
                    <tr>
                        <th>Hora</th>
                        {report && report.establishments && report.establishments.map(() => {
                            return ([
                                <th key="invoicesHeader">Facturas</th>,
                                <th key="netHeader">Neto</th>,
                                <th key="grossHeader">Bruto</th>]);
                        })}
                    </tr>
                    {!report.periodNames || report.periodNames.map((periodName, i) => {
                        return (
                            [
                            <tr key="establishmentNames"><td/>
                            {report.establishments.map((establishment) => {
                                return (<td key={establishment.name} colSpan={3}>{establishment.name}</td>);
                            })}
                            </tr>,
                            <tr key={i}>
                                <td>{periodName}</td>
                                {report.establishments.map((establishment) => {
                                    const netTotal = establishment.netTotals[i];
                                    const grossTotal = establishment.grossTotals[i];
                                    const invoices = establishment.numberOfInvoices[i];
                                    const cellClass = invoices !== "0" ? "cellData": "emptyCell";
                                    return (
                                      [<td key={"i"+i} data-th="Facturas"
                                           className={cellClass}>{invoices}</td>,
                                       <td key={"n"+i} data-th="Neto"
                                           className={cellClass}>{netTotal}</td>,
                                       <td key={"g"+i} data-th="Bruto"
                                           className={cellClass}>{grossTotal}</td>,
                                      ]);
                                })}
                            </tr>]
                        );
                    })}
                    </tbody>
                </table>
            </Grid>
        );
    }
}

HourlyReportPage.propTypes = {
    router: PropTypes.object.isRequired,
    establishments: PropTypes.array,
    actions: PropTypes.object.isRequired,
    notifierActions: PropTypes.object.isRequired,
    notifications: PropTypes.object.isRequired,
    report: PropTypes.object
};
