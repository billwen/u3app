import React, { useState, useEffect } from "react";
import {listProducts, Product} from "./ProductsService";
import ProductCard from "./ProductCard";
import {useLocation} from "react-router";

const ProductsIndex = () => {
    const [products, setProducts] = useState<Product[]>([]);
    const {state} = useLocation();
    console.log(state);

    useEffect(() => {
        if (state) {
            console.warn(`Nothing found for ${state.id ?? "no id"}`);
        }
    }, [state]);

    useEffect( () => {
        (async () => {
            const data = await listProducts();
            setProducts(data);
        })();
    }, []);

    if (products == null) {
        return (<div>Loading ...</div>);
    } else {
        return (
            <div>
                { products.map( item => {
                    return (
                        <ProductCard key={item.id} product={item} />
                    );
                })}
            </div>
        );
    }

}

export default ProductsIndex;
