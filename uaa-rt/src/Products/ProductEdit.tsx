import React, {useEffect, useState} from "react";
import {css} from "@emotion/css";
import {createProduct, Product} from "./ProductsService";

const ProductEditStyle = css`
  display: flex;
  color: #fff;
  background: #2a2c37;
  border-radius: 6px;
  padding: 15px;
`;

interface UpdateFieldParam {
    name: string;
    value: string | number | null;
}

const ProductEdit = () => {
    const emptyProduct: Product = {
        id: "",
        name: "",
        description: "",
        price: 0
    }

    const [form, setForm] = useState<Product | null>(null);

    // Loading Item
    useEffect( () => {
        setForm({
            id: '',
            name: '',
            description: '',
            price: 0
        });
    }, [])

    const updateField = ({name, value}: UpdateFieldParam) => {
        if (form === null) {
            return;
        }

        setForm({
            ...form,
            [name]: value
        });
    }

    const handleCreate = async () => {
        if (form === null) {
            return;
        }

        try {
            const created = await createProduct(form);

        } catch (e) {
            console.warn(e);
        }
    }
    if (form === null) {
        return <div>Loading ...</div>;
    }

    return (
        <form className={ProductEditStyle}>
            <input type="text" name="id" placeholder="ID" className="ProductEdit-Input" onChange={ ({target}) => updateField(target)} value={form.id} />
            <input type="text" name="name" placeholder="Name" className="ProductEdit-Input" onChange={ ({target}) => updateField(target)} value={form.name} />
            <input type="text" name="price" placeholder="Price" className="ProductEdit-Input" onChange={ ({target}) => updateField({name: target.name, value: parseInt(target.value, 10)})} value={form.price} />
            <textarea name="description" placeholder="Description" className="ProductEdit-Input ProductEdit-Textarea" onChange={ ({target}) => updateField(target)} value={form.description}/>

            <button type="button" className="ProductEdit-Button" onClick={handleCreate}>Create</button>
        </form>
    );
}

export default ProductEdit;