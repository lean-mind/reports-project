import React, {PropTypes} from 'react';
import { Link as ReactLink } from 'react-router';
import AppBar from 'material-ui/AppBar';
import * as paths from '../routePaths';
import { Grid, Row, Col as UndecoratedCol } from 'react-bootstrap';
import styles from '../styles/app-layout';
import Radium from 'radium';
const Link = Radium(ReactLink);
const Col = Radium(UndecoratedCol);

class AppLayout extends React.Component {
    render() {
        return (
            <div className="app-layout">
                <AppBar
                    title="Reports"
                    iconClassNameRight="muidocs-icon-navigation-expand-more"
                />

                <Grid>
                    <Row style={styles.reports}>
                        <Col sm={3} style={[styles.reportSelector, styles.reportSelector.first]}>
                            <Link to={paths.REPORTS_TOTALS}
                                  style={styles.reportLink}
                                  activeStyle={styles.reportLink.active}>
                                <div style={[styles.report, this.props.location.pathname == paths.REPORTS_TOTALS && styles.report.active]} key="report1">
                                    <div style={[styles.report.title, this.props.location.pathname == paths.REPORTS_TOTALS && styles.report.title.active]}>
                                        Ventas totales
                                    </div>
                                    <div style={styles.report.description}>
                                        Muestra una tabla con el número de facturas, el importe neto, bruto y medio bruto por local seleccionado en un intervalo de fechas.
                                    </div>
                                </div>
                            </Link>
                        </Col>

                        <Col sm={3} style={styles.reportSelector}>
                            <Link to={paths.REPORTS_PERIOD}
                                  style={styles.reportLink}
                                  activeStyle={styles.reportLink.active}>
                                <div style={[styles.report, this.props.location.pathname == paths.REPORTS_PERIOD && styles.report.active]} key="report2">
                                    <div style={[styles.report.title, this.props.location.pathname == paths.REPORTS_PERIOD && styles.report.title.active]}>
                                        Ventas por dia y hora
                                    </div>
                                    <div style={styles.report.description}>
                                        Muestra las ventas por cada dia y cada hora, para cada establecimiento.
                                    </div>
                                </div>
                            </Link>
                        </Col>

                        <Col sm={3} style={styles.reportSelector}>
                            <Link to={paths.REPORTS_GROUPED_HOURS}
                                  style={styles.reportLink}
                                  activeStyle={styles.reportLink.active}>
                                <div style={[styles.report, this.props.location.pathname == paths.REPORTS_GROUPED_HOURS && styles.report.active]} key="report2">
                                    <div style={[styles.report.title, this.props.location.pathname == paths.REPORTS_GROUPED_HOURS && styles.report.title.active]}>
                                        Ventas agrupadas por horas
                                    </div>
                                    <div style={styles.report.description}>
                                        Un informe similar al de ventas totales pero que agrupa los datos por las horas que se facturó.
                                    </div>
                                </div>
                            </Link>
                        </Col>

                        <Col sm={3} style={[styles.reportSelector, styles.reportSelector.last]}>
                            <Link to={paths.REPORTS_HISTORY}
                                  style={styles.reportLink}
                                  activeStyle={styles.reportLink.active}>
                                <div style={[styles.report, this.props.location.pathname == paths.REPORTS_HISTORY && styles.report.active]} key="report3">
                                    <div style={[styles.report.title, this.props.location.pathname == paths.REPORTS_HISTORY && styles.report.title.active]}>
                                        Histórico de ventas
                                    </div>
                                    <div style={styles.report.description}>
                                        Un gráfico lineal que recoge el importe bruto de ventas por local y muestra su evolución en distintos intervalos de tiempo.
                                    </div>
                                </div>
                            </Link>
                        </Col>
                    </Row>
                </Grid>
                {this.props.children}
            </div>
        );
    }
}

AppLayout.propTypes = {
    children: PropTypes.element,
    location: PropTypes.object
};

export default Radium(AppLayout);
