import React from 'react';
import {Navigate, Route} from 'react-router-dom';

function PrivateRoute({component: Component, ...rest}) {
    return (
        <Route
            {...rest}
            render={props =>
                localStorage.getItem('mint-token') ? (
                    <Component {...props} />
                ) : (
                    <Navigate to="/login" />
                )
            }
        />
    )
}

export default PrivateRoute;