import React, {useReducer} from "react";
import weekReducer from "./weekReducer";
import {getWeek} from "utils/date-wrangler";
import {FaCalendarDay, FaChevronLeft, FaChevronRight} from "react-icons/fa";

export interface WeekPickerProps {
    date: Date;
};

function WeekPicker({date}: WeekPickerProps) {
    const [week, dispatch] = useReducer(weekReducer, date, getWeek);

    return (
        <div>
            <p className="date-picker">
                <button className="btn" onClick={ () => dispatch({type: "PREV_WEEK"}) }>
                    <FaChevronLeft />
                    <span>Prev</span>
                </button>
                <button className="btn" onClick={ () => dispatch({type: "TODAY"}) }>
                    <FaCalendarDay />
                    <span>Today</span>
                </button>
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