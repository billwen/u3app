import React from "react";
import {FaSpinner} from "react-icons/fa";

const Spinner = (props: {string?: any}) => {
    return (
        <span {...props}>
            <FaSpinner className="icon-loading" />
        </span>
    );
}

export default Spinner;