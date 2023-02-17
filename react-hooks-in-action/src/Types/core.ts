import {Bookable} from "./domain";

export type AppState = {
    group: string;
    bookableIndex: number;
    showDetails: boolean;
    bookables: Bookable[];

    isLoading: boolean;
    error: string | null;
};

export const initialAppState: AppState = {
  group: "Rooms",
  bookableIndex: 0,
  showDetails: true,
  bookables: [],
  isLoading: true,
  error: null
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
} | {
    type: "FETCH_BOOKABLES_REQUEST";
} | {
    type: "FETCH_BOOKABLES_SUCCESS";
    payload: Bookable[];
} | {
    type: "FETCH_BOOKABLES_ERROR";
    payload: string;
};