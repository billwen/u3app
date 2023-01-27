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