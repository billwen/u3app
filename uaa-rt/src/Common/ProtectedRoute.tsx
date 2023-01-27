import React from "react";
import {Navigate} from "react-router-dom";

// @ts-ignore
const ProtectedRoute = ( {authenticated, redirectTo, children }) => {

    if (!authenticated) {
        return <Navigate to={redirectTo} />
    }

    return children;
}

export default ProtectedRoute;