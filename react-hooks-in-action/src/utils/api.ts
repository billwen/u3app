export default function getData<T>(url: string): Promise<T> {
    return fetch(url).then( resp => {
        if (!resp.ok) {
            throw Error("There was a problem fetching data.");
        }
        
        return resp.json() as Promise<T>;
    });
}