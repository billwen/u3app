import React, { useState, useEffect } from "react";
import {listProducts, Product} from "./ProductsService";
import ProductCard from "./ProductCard";

const ProductsIndex = () => {
    const [products, setProducts] = useState<Product[]>([]);

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
