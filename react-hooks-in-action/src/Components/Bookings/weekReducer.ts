import {Week} from "Types/domain";
import {getWeek} from "utils/date-wrangler";

export type BookingActionType =
    | {
    type: "NEXT_WEEK"
} | {
    type: "PREV_WEEK"
} | {
    type: "TODAY"
} | {
    type: "SET_DATE";
    payload: number;
};

export default function weekReducer(state: Week, action: BookingActionType): Week {
    switch (action.type) {
        case "NEXT_WEEK":
            return getWeek(state.date, 7);

        case "PREV_WEEK":
            return getWeek(state.date, -7);

        case "TODAY":
            return getWeek(new Date());

        case "SET_DATE":
            return getWeek(new Date(action.payload));

        default:
            throw new Error(`Unknown action: ${action}`);
    }
}