import React, {PropTypes} from 'react';
import { Link as ReactRouterLink } from 'react-router';
import * as paths from '../routePaths';
import { RaisedButton, TextField } from 'material-ui';
import LoginStyles from '../styles/login-page';
import { Row, Col } from 'react-bootstrap';
import Radium from 'radium';
const Link = Radium(ReactRouterLink);

class LoginPage extends React.Component {
    render() {
        const error = this.props.router.location.search;
        const errorText = error ? "Usuario o contraseña incorrectos" : "";
        return (
            <Row>
                <Col xs={10} xsOffset={1} sm={6} smOffset={3} md={4} mdOffset={4}>
                    <div style={LoginStyles.root}>
                        <form action="/login" method="POST" style={LoginStyles.form}>
                            <div style={LoginStyles.loginError}>
                                {errorText}
                            </div>
                            <TextField name="username"
                                    className="text-field"
                                    style={LoginStyles.textField}
                                    floatingLabelStyle={LoginStyles.textField.label}
                                    floatingLabelText="Usuario"/>
                            <TextField name="password"
                                    type="password" 
                                    style={LoginStyles.textField}
                                    floatingLabelStyle={LoginStyles.textField.label}
                                    floatingLabelText="Contraseña"/>
                            <RaisedButton id="login-button"
                                        name="submitLogin"
                                        type="submit"
                                        style={LoginStyles.raisedButton}
                                        label="Iniciar Sesión"
                                        primary={true}
                                        fullWidth={true}/>
                            <Link to={paths.FORGOT_PASSWORD} style={LoginStyles.forgotPasswordLink}>
                                ¿Olvidaste tu contraseña?
                            </Link>
                        </form>
                    </div>
                </Col>
            </Row>
        );
    }
}

LoginPage.propTypes = {
    router: PropTypes.object.isRequired
};

export default Radium(LoginPage);