import { colorPalette } from './common';

const LoginStyles = function() {

    const root = {
        maxWidth: '450px',
        margin: '200px auto 0 auto',
        '@media (max-width: 767px)': {
            marginTop: '50px'
        }
    };

    const form = {
       textAlign: 'center'
    };

    const loginError = {
        color: colorPalette.red
    };

    const textField = {
        width: '100%',
        label: {
            fontWeight: 'normal'
        }
    };

    const raisedButton = {
        margin: '14px 0 10px 0',
    };

    const forgotPasswordLink = {
        fontSize: '14px',
        color: colorPalette.darkBlue,
        textDecoration: 'none',
        transition: 'color 0.2s ease-in-out',
        ':hover': {
            color: colorPalette.lightBlue
        }
    };

    return {
        root,
        form,
        loginError,
        textField,
        raisedButton,
        forgotPasswordLink
    };
}();

export default LoginStyles;
