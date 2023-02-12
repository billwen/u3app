type Item = {
    id: string;
}

export const findItemIndexById = <T extends Item>(items: T[], id: string): number => {
    return items.findIndex( (item: T) => item.id === id);
}

export const removeItemAtIndex = <T extends Item>(array: T[], index: number): T[] => {
    return [...array.slice(0, index), ...array.slice(index+1)];
}

export const insertItemAtIndex = <T extends Item>(array: T[], item: T, index: number): T[] => {
    return [...array.slice(0, index), item, ...array.slice(index)];
}

export const moveItem = <T extends Item>(array: T[], from: number, to: number): T[] => {
    const item = array[from];
    return insertItemAtIndex(removeItemAtIndex(array, from), item, to);
}