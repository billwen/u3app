export type Bookable = {
    id: number;
    group: string;
    title: string;
    notes: string;
    sessions: number[];
    days: number[];
};

export type User = {
    id: number;
    name: string;
    img: string;
    title: string;
    notes: string;
};

export type Week = {
    date: Date;
    start: Date;
    end: Date;
}