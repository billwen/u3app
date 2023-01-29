const api_base = process.env.REACT_APP_API_BASE;
console.log("API base " + api_base);

export interface Product {
    id: string;
    name: string;
    description: string;
    price: number;
}

export const listProducts : () => Promise<Product[]> = async () => {
    const response = await fetch(`${api_base}/api/products`);
    if (response.ok) {
        return await response.json();
    }

    throw new Error('Error while downloading data')
};

export const retrieveProduct = async (id: string) => {
    const response = await fetch(`${api_base}/api/products/${id}`);
    if (response.ok) {
        return await response.json();
    }

    throw new Error("Error, while downloading details")
}

export const createProduct = async (form: Product) => {
    const response = await fetch(`${api_base}/api/products`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }    ,
        body: JSON.stringify(form)
    })

    if (response.ok) {
        return await response.json();
    }

    throw new Error("Error, while creating a new product");
}

export const updateProduct = async (form: Product) => {
    const response = await fetch(`${api_base}/api/products/${form.id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }    ,
        body: JSON.stringify(form)
    })

    if (response.ok) {
        return await response.json();
    }

    throw new Error("Error, while updating a new product");
}

export const deleteProduct = async (id: String) => {
    const response = await fetch(`${api_base}/api/products/${id}`, {
        method: 'DELETE'
    })

    if (response.ok) {
        return await response.json();
    }

    throw new Error("Error, while deleting a new product");
}