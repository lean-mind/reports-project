import React, {PropTypes} from 'react';
import * as apiRoutes from '../apiRoutes';
import { Grid as TempGrid, Row, Col } from 'react-bootstrap';
import styles from '../styles/totals-report';
import { FaFileExcelO, FaFilePdfO } from 'react-icons/lib/fa';
import Radium from 'radium';
import ReportPage from './ReportPage';

const Grid = Radium(TempGrid);

export class TotalsReportPage extends ReportPage {

    requestReport(filters){
        return this.props.actions.showReport(filters);
    }

    renderReport(){
        if (!this.props.report) return null;
        let {report} = this.props;
        let spreadsheetUrl = apiRoutes.REPORTS_TOTALS_SPREADSHEET + this.props.router.location.search;
        let pdfUrl = apiRoutes.REPORTS_TOTALS_PDF + this.props.router.location.search;
        return (
            <Grid style={styles.root}>
                <Row id="download-buttons">
                    <Col xs={6} sm={3} smOffset={6} lg={2} lgOffset={8}>
                        <a href={spreadsheetUrl} target="_blank" style={styles.downloadLink} key="excelLink">
                            <div style={[styles.downloadButton, styles.downloadButton.excel]} key="excelButton">
                                <FaFileExcelO style={styles.excelIcon}/>
                                Descargar Excel
                            </div>
                        </a>
                    </Col>
                    <Col xs={6} sm={3} lg={2}>
                        <a href={pdfUrl} target="_blank" style={styles.downloadLink} key="pdfLink">
                            <div style={[styles.downloadButton, styles.downloadButton.pdf]} key="pdfButton">
                                <FaFilePdfO style={styles.pdfIcon}/>
                                Descargar PDF
                            </div>
                        </a>
                    </Col>
                </Row>
                <table id="totals-summary" className="responsive-table summary-by-establishment">
                    <tbody>
                        <tr>
                            <th>Local</th>
                            <th>Número de Facturas</th>
                            <th>Importe Total Neto</th>
                            <th>Importe Total Bruto</th>
                            <th>Importe Medio Bruto</th>
                        </tr>
                        <tr className="mobile-header">
                            <th>Informe de Ventas Totales</th>
                        </tr>
                        {!report.establishments || report.establishments.map((establishment, i) => {
                            return (
                                <tr key={i}>
                                    <td data-th="Local">{establishment.name}</td>
                                    <td data-th="Número de Facturas">{establishment.numberOfInvoices}</td>
                                    <td data-th="Importe Total Neto">{establishment.netTotals}</td>
                                    <td data-th="Importe Total Bruto">{establishment.grossTotals}</td>
                                    <td data-th="Import Medio Bruto">{establishment.averageGrossTotals}</td>
                                </tr>
                            );
                        })}
                        <tr>
                            <td data-th="Todos los Locales">Total</td>
                            <td data-th="Número de Facturas">{report.totalNumberOfInvoices}</td>
                            <td data-th="Importe Total Neto">{report.totalNetAmount}</td>
                            <td data-th="Importe Total Bruto">{report.totalGrossAmount}</td>
                            <td data-th="Import Medio Bruto">{report.totalAverageGrossTotals}</td>
                        </tr>
                    </tbody>
                </table>
            </Grid>
        );
    }
}

TotalsReportPage.propTypes = {
    router: PropTypes.object.isRequired,
    establishments: PropTypes.array,
    actions: PropTypes.object.isRequired,
    notifierActions: PropTypes.object.isRequired,
    notifications: PropTypes.object.isRequired,
    report: PropTypes.object
};

