import { colorPalette } from './common';

const TotalsReportStyles = function() {
    
    const root = {
        marginTop: '40px',
        marginBottom: '60px',
        '@media (max-width: 767px)': {
            marginBottom: '10px'
        }
    };

    const downloadLink = {
        ':hover': {
            textDecoration: 'none'
        },
        ':visited': {
            textDecoration: 'none'
        },
        ':focus': {
            textDecoration: 'none'
        }
    };

    const downloadButton = {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '10px',
        color: colorPalette.blackText,
        border: `1px solid ${colorPalette.grey}`,
        transition: 'background-color 0.18s ease-in-out',
        excel: {
            backgroundColor: colorPalette.greenTransparent,
            ':hover': {
                backgroundColor: colorPalette.greenTransparentHover
            },
            ':active': {
                backgroundColor: colorPalette.greenTransparentActive
            }
        },
        pdf: {
            backgroundColor: colorPalette.redTransparent,
            ':hover': {
                backgroundColor: colorPalette.redTransparentHover
            },
            ':active': {
                backgroundColor: colorPalette.redTransparentActive
            }
        }
    };

    const excelIcon = {
        fill: 'green',
        fontSize: '17px',
        marginRight: '10px'
    };

    const pdfIcon = {
        fill: 'red',
        fontSize: '17px',
        marginRight: '10px'
    };

    return {
        root,
        downloadLink,
        downloadButton,
        pdfIcon,
        excelIcon
    };

}();

export default TotalsReportStyles;