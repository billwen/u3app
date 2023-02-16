import React, {ChangeEvent, useReducer} from "react";
import {FaArrowRight} from "react-icons/fa";
import {Bookable} from "Types/domain";
import {initialAppState} from "Types/core";
import reducer from "./reducer";

import db from "../../db.json";


const { days, sessions} : { days: string[], sessions: string[]} = db;

const BookablesList = () => {

    const [state, dispatch] = useReducer(reducer, initialAppState);
    const {group, bookableIndex, bookables, showDetails} = state;

    const bookablesInGroup: Bookable[] = bookables.filter( b => b.group === group );
    const groups = [...new Set(bookables.map(b => b.group))];
    const bookable = bookablesInGroup[bookableIndex];

    function changeGroup(e: ChangeEvent<HTMLSelectElement>) {
        dispatch({
            type: "SET_GROUP",
            payload: e.target.value
        });
    }

    function changeBookable(selectedIndex: number) {
        dispatch({
            type: "SET_BOOKABLE",
            payload: selectedIndex
        });
    }

    function nextBookable() {
        dispatch({
            type: "NEXT_BOOKABLE"
        });
    }

    function toggleDetails() {
        dispatch({
            type: "TOGGLE_SHOW_DETAILS"
        });
    }

    return (
        <>
            <div>
                <select value={group} onChange={ changeGroup }>
                    {
                        groups.map( g => <option value={g} key={g}>{g}</option>)
                    }
                </select>

                <ul className="bookables items-list-nav">
                    { bookablesInGroup.map( (b, i) => (
                            <li key={b.id} className={ i === bookableIndex ? "selected" : undefined }>
                                <button className="btn" onClick={ () => changeBookable(i) }>{b.title}</button>
                            </li>
                        )
                    ) }
                </ul>

                <p>
                    <button className="btn" onClick={nextBookable} autoFocus>
                        <FaArrowRight />
                        <span>Next</span>
                    </button>
                </p>
            </div>

            { bookable && (
                <div className="bookable-details">
                    <div className="item">
                        <div className="item-header">
                            <h2>{bookable.title}</h2>
                            <span className="controls">
                                <label>
                                    <input type="checkbox" checked={showDetails} onChange={ () => toggleDetails() } />
                                    Show Details
                                </label>
                            </span>
                        </div>
                        <p>{bookable.notes}</p>

                        { showDetails && (
                            <div className="item-details">
                                <h3>Availability</h3>
                                <div className="bookable-availability">
                                    <ul>
                                        {bookable.days.sort().map( d => <li key={d}>{days[d]}</li>) }
                                    </ul>

                                    <ul>
                                        {bookable.sessions.map( s => <li key={s}>{sessions[s]}</li>)}
                                    </ul>
                                </div>
                            </div>
                        ) }
                    </div>
                </div>
            ) }
        </>


    );
};

export default BookablesList;