import React, {useReducer, useRef} from "react";
import weekReducer from "./weekReducer";
import {getWeek} from "utils/date-wrangler";
import {FaCalendarCheck, FaCalendarDay, FaChevronLeft, FaChevronRight} from "react-icons/fa";

export interface WeekPickerProps {
    date: Date;
};

function WeekPicker({date}: WeekPickerProps) {
    const [week, dispatch] = useReducer(weekReducer, date, getWeek);
    const textboxRef = useRef<HTMLInputElement>(null);

    function goToDate() {
        if (textboxRef.current ) {
            const dateString = textboxRef.current.value;
            const date = Date.parse(dateString);

            dispatch({
                type: "SET_DATE",
                payload: date
            });
        }
    }
    return (
        <div>
            <p className="date-picker">
                { /*  Prev button */ }
                <button className="btn" onClick={ () => dispatch({type: "PREV_WEEK"}) }>
                    <FaChevronLeft />
                    <span>Prev</span>
                </button>

                { /* Today button */ }
                <button className="btn" onClick={ () => dispatch({type: "TODAY"}) }>
                    <FaCalendarDay />
                    <span>Today</span>
                </button>

                <span>
                    <input type="text" ref={textboxRef} placeholder="e.g. 2020-09-02" defaultValue="2020-06-24" />
                    <button className="go btn" onClick={goToDate}>
                        <FaCalendarCheck />
                        <span>Go</span>
                    </button>
                </span>

                { /* Next Button */ }
                <button className="btn" onClick={ () => dispatch({type: "NEXT_WEEK"}) }>
                    <span>Next</span>
                    <FaChevronRight />
                </button>
            </p>

            <p>
                {week.start.toDateString()} - {week.end.toDateString()}
            </p>
        </div>
    );
};

export default WeekPicker;