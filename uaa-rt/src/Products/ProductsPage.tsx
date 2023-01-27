import React from "react";
import { css } from '@emotion/css'
import {Route, Routes} from "react-router-dom";
import ProductsIndex from "./ProductsIndex";
import ProductDetailPage from "./ProductDetailPage";

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
            <Routes>
                <Route path="/" element={<ProductsIndex />}  />
                <Route path=":id" element={<ProductDetailPage />}/>
            </Routes>
        </div>
    );
}

export default ProductsPage;
