import {ActionType, AppState} from "Types/core";

export default function reducer(state: AppState, action: ActionType): AppState {
    switch (action.type) {
        case "SET_GROUP":
            return {
                ...state,
                group: action.payload,
                bookableIndex: 0
            };

        case "SET_BOOKABLE":
            return {
                ...state,
                bookableIndex: action.payload
            };

        case "TOGGLE_SHOW_DETAILS":
            return {
                ...state,
                showDetails: !state.showDetails
            };

        case "NEXT_BOOKABLE":
            const count = state.bookables.filter( b => b.group === state.group ).length;
            return {
                ...state,
                bookableIndex: (state.bookableIndex + 1) % count
            };

        case "FETCH_BOOKABLES_ERROR":
            return {
                ...state,
                isLoading: false,
                error: action.payload
            };

        case "FETCH_BOOKABLES_REQUEST":
            // Clear the bookables when requesting new data
            return {
                ...state,
                isLoading: true,
                error: null,
                bookables: []
            };

        case "FETCH_BOOKABLES_SUCCESS":
            // Pass the loaded bookables to the reducer via the payload
            return {
                ...state,
                isLoading: false,
                bookables: action.payload
            };

        default:
            throw new Error("Invalid action type");
    }
}