import { colorPalette } from './common';

const AppStyles = function() {

    const reports = {
        margin: '10px 0'
    };

    const reportSelector = {
        padding: '20px 20px 20px 20px',
        '@media (max-width: 767px)': {
            padding: '10px 0 10px 0'
        },
        first: {
            padding: '20px 20px 20px 0'
        },
        last: {
            padding: '20px 0 20px 20px'
        },
        
    };

    const reportLink = {
        ':hover': {
            textDecoration: 'none'
        },
        active: {
            textDecoration: 'none'
        }
    };

    const report = {
        padding: '20px 30px',
        height: '160px',
        border: `1px solid ${colorPalette.grey}`,
        color: colorPalette.blackUnselectedText,
        transition: 'background-color 0.18s ease-in-out',
        ':hover': {
            backgroundColor: colorPalette.lightBlueTransparentHover
        },
        ':active': {
            backgroundColor: colorPalette.lightBlueTransparentActive
        },
        active: {
            color: colorPalette.blackText,
            backgroundColor: colorPalette.lightBlueTransparentChecked
        },
        title: {
            color: colorPalette.blackUnselectedText,
            fontWeight: 'bold',
            marginBottom: '10px',
            active: {
                color: colorPalette.blackText
            }
        },
        description: {
            textAlign: 'justify'
        }
    };

    return {
        reports,
        reportSelector,
        reportLink,
        report
    };
}();

export default AppStyles;
