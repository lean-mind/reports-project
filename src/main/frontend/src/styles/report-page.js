import { colorPalette } from './common';

const ReportPageStyles = function() {

    const establishmentCheckbox = {
        display: 'block',
        padding: '10px 6px 6px 10px',
        marginBottom: '15px',
        border: `1px solid ${colorPalette.grey}`,
        transition: 'all 0.18s ease-in-out',
        checked: {
            backgroundColor: colorPalette.lightBlueTransparentActive
        },
        input: {
            top: 0,
        },
        icon: {
            marginRight: '8px',
            fill: colorPalette.darkGreyText
        },
        label: {
            whiteSpace: 'nowrap',
            textOverflow: 'ellipsis',
            overflow: 'hidden',
            width: '100%',
            color: colorPalette.blackUnselectedText
        }
    };

    const datePicker = {
        textField: {
            width: '100%'
        },
        floatingLabel: {
            color: colorPalette.darkGreyText
        }
    };

    const raisedButton = {
        width: '100%',
        margin: '10px 0',
    };

    const dynamic = (styles) => {
        if (styles[1] == true) {
            return Object.assign({}, styles[0], {backgroundColor: 'red'});
        }
        return styles[0];
    };
 
    return {
        establishmentCheckbox,
        datePicker,
        raisedButton,
        dynamic
    };

}();

export default ReportPageStyles;
