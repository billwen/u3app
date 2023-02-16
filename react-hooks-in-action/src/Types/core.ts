import {Bookable} from "./domain";
import db from "../db.json";

export type AppState = {
    group: string;
    bookableIndex: number;
    showDetails: boolean;

    bookables: Bookable[];
};

const { bookables}: {bookables: Bookable[]} = db;

export const initialAppState: AppState = {
  group: "Rooms",
  bookableIndex: 0,
  showDetails: true,
  bookables: bookables
};

export type ActionType =
    | {
    type: "SET_GROUP";
    payload: string;
} | {
    type: "SET_BOOKABLE";
    payload: number;
} | {
    type: "TOGGLE_SHOW_DETAILS";
} | {
    type: "NEXT_BOOKABLE";
};