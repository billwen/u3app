import React from "react";
import { Outlet } from "react-router";
import { css } from '@emotion/css'

const ProductsStyles = css`
display: flex;
flex-direction: column;

.Logo {
    width: 125px;
    margin: 0 auto 25px;
}
`;

const ProductsPage = () => {
    return (
        <div className={ProductsStyles}>
            <img src={process.env.PUBLIC_URL + '/assets/img/logo.svg'} alt="Ultimate Burgers" className="Logo"/>
            <Outlet />
        </div>
    );
}

export default ProductsPage;
