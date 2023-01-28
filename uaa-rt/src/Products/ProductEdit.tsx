import React, {useEffect, useState} from "react";
import {css} from "@emotion/css";
import {createProduct, Product} from "./ProductsService";
import {useNavigate} from "react-router-dom";

const ProductEditStyle = css`

  color: #fff;
  background: #2a2c37;
  border-radius: 6px;
  padding: 15px;
  
  .ProductEdit {
    &-Input {
      width: 100%;
      border: 1px solid transparent;
      color: #fff;
      background: #1d1e26;
      padding: 10px 15px;
      margin-bottom: 5px;
      border-radius: 6px;
      outline: 0;
      
      &:focus {
        border-color: #50fa7b;
      }
    }
    
    &-Textarea {
      min-height: 80px;
      resize: none;
    }
    
    &-Button {
      border: 2px solid #50fa7b;
      color: #50fa7b;
      background: none;
      padding: 10px 15px;
      border-radius: 6px;
      outline: 0;
      cursor: pointer;
      font-weight: 600;
      text-transform: uppercase;
    }
  }
`;

interface UpdateFieldParam {
    name: string;
    value: string | number | null;
}

const ProductEdit = () => {
    const navigate = useNavigate();
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
            navigate(`/admin/${created.id}`);
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