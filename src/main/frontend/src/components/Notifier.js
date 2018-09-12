import React, {PropTypes} from 'react';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import CircularProgress from 'material-ui/CircularProgress';
import Radium from 'radium';

export class Notifier extends React.Component {
    render() {
        const {notifications} = this.props;
        const {actions} = this.props;
        return (
            <div>
                <Dialog title={notifications.dialogState.text}
                        open={notifications.dialogState.isOpen}
                        style={{textAlign: 'center'}}
                        modal={true}
                        actions={[
                            <FlatButton
                                key="closeDialogButton"
                                id="closeDialogButton"
                                onClick={() => actions.hideDialog()}>
                                Cerrar
                            </FlatButton>
                        ]}
                />
                <Dialog title={notifications.spinnerState.text}
                        open={notifications.spinnerState.isOpen}
                        modal={true}
                        style={{textAlign: 'center'}}
                actions={[
                    <FlatButton
                        key="closeSpinnerButton"
                        id="closeSpinnerButton"
                        onClick={() => actions.slowOperationFinished()}>
                        Cerrar
                    </FlatButton>
                ]}>
                        <CircularProgress/>
                </Dialog>
            </div>
        );
    }
}

Notifier.propTypes = {
    notifications: PropTypes.object.isRequired,
    actions: PropTypes.object.isRequired
};

export default Radium(Notifier);
