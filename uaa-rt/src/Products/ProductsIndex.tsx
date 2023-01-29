import React, { useState, useEffect, ChangeEventHandler } from "react";
import {useLocation, useSearchParams} from "react-router-dom";
import {css} from "@emotion/css";

import {listProducts, Product} from "./ProductsService";
import ProductCard from "./ProductCard";

const ProductsIndexStyles = css`
    
    .ProductsIndex {
      &-Radios {
        display: flex;
        align-items: center;
        
        span {
          width: 65px;
          color: #fff;
          font-size: 0.8rem;
          margin-right: 10px;
        }
        
        label {
          display: flex;
          align-items: center;
          cursor: pointer;
        }
      }
      
      &-List {
        margin-top: 10px;
      }
    }
`;

const ProductsIndex = () => {
    const [searchParams, setSearchParams] = useSearchParams();

    const [products, setProducts] = useState<Product[]>([]);
    const {state} = useLocation();

    // display redirect message
    useEffect(() => {
        if (state) {
            console.warn(`Nothing found for ${state.id ?? "no id"}`);
        }
    }, [state]);

    useEffect( () => {
        (async () => {
            const data: Product[] = await listProducts();
            const params: {[p: string]: string} = Object.fromEntries([...searchParams]);
            sortProductsFromParams(data, params);
        })();
    }, []);

    const updateParams: ChangeEventHandler<HTMLInputElement> = (e) => {
        const currentParams = Object.fromEntries([...searchParams]);
        const {name, value} = e.target;
        const newParams = {
            ...currentParams,
            [name]: value
        }
        setSearchParams(newParams);
        sortProductsFromParams(products, newParams);
    }

    const sortProductsFromParams = (data: Product[], params: {[p: string]: string}) => {
        console.log("sort");
        if (!Object.keys(params).length) {
            setProducts(data);
            return;
        }

        // @ts-ignore
        const sorted = [...data].sort( (x, y) => {
            const {sort, order} = params;
            switch (order) {
                case 'ascending': {
                    // @ts-ignore
                    return x[sort] > y[sort] ? 1 : -1;
                }

                case 'descending': {
                    // @ts-ignore
                    return x[sort] > y[sort] ? -1 : 1;
                }
            }
        });
        setProducts(data);
    };

    if (products == null) {
        return (<div>Loading ...</div>);
    } else {
        return (
            <div className={ProductsIndexStyles}>
                <div className="ProductsIndex-Radios">
                    <span>Sort:</span>
                    <label>Name <input type="radio" name="sort" value="name" onChange={updateParams} defaultChecked={searchParams.get('sort') == 'name'} /></label>
                    <label>Price <input type="radio" name="sort" value="price" onChange={updateParams} defaultChecked={searchParams.get('sort') == 'price'} /></label>
                </div>
                <div className="ProductsIndex-Radios">
                    <span>Order by:</span>
                    <label>Ascending <input type="radio" name="order" value="ascending" onChange={updateParams} defaultChecked={searchParams.get('order') == 'ascending'} /></label>
                    <label>Descending <input type="radio" name="order" value="descending" onChange={updateParams} defaultChecked={searchParams.get('order') == 'descending'} /></label>
                </div>
                <div className="ProductsIndex-List">
                    { products.map( item => {
                        return (
                            <ProductCard key={item.id} product={item} />
                        );
                    })}
                </div>
            </div>

        );
    }

}

export default ProductsIndex;
