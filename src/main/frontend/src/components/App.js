import React, {PropTypes} from 'react';
import { StyleRoot } from 'radium';

class App extends React.Component {
    render() {
        return (
            <StyleRoot>
                {this.props.children}
            </StyleRoot>
        );
    }
}

App.propTypes = {
    children: PropTypes.element
};

export default App;
