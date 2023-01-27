import React from "react";
import {Link} from "react-router-dom";
import {Product} from "./ProductsService";
import { css } from "@emotion/css"

export interface ProductCardProp {
    product: Product;
}

const ProductCardStyle = css`
  display: flex;
  color: #fff;
  background: #2a2c37;
  border-radius: 6px;
  text-decoration: none;
  padding: 15px;
  margin-bottom: 5px;
  transition: transform .1s ease-in-out, background .1s ease-in-out, box-shadow .1s ease-in-out;
  
  &:hover {
    transform: translate(0, -3px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, .0.35);
  }
  
  .ProductCard {
    &-Icon {
      width: 40px;
      margin-right: 15px;
    }
    
    &-Name {
      font-size: 1.2rem;
      margin: 0
    }
    
    &-Price {
      color: #50fa7b;
      font-size: 1rem;
      margin: 0;
    }
  }
`;

const ProductCard = ({product}: ProductCardProp) => {
    return (
        // Absoulte path
        // <Link to={`/product/${product.id}`} className={ProductCardStyle}>
        <Link to={product.id} className={ProductCardStyle}>
            <img src={process.env.PUBLIC_URL + '/assets/img/products/' + product.id + '.svg'} alt={product.name} className="ProductCard-Icon" />
            <div>
                <h2 className="ProductCard-Name">{product.name}</h2>
                <p className="ProductCard-Price">{`$${product.price / 100}`}</p>
            </div>
        </Link>
    );
}

export default ProductCard;