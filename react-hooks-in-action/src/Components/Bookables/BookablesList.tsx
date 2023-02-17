import React, {ChangeEvent, useEffect, useReducer, useRef, useState} from "react";
import {FaArrowRight} from "react-icons/fa";
import {Bookable} from "Types/domain";
import {initialAppState} from "Types/core";
import reducer from "./reducer";
import getData from "utils/api";
import Spinner from "Components/UI/Spinner";

const BookablesList = () => {

    const [days, setDays] = useState<string[] | null>(null);
    const [sessions, setSessions] = useState<string[] | null>(null);
    const [state, dispatch] = useReducer(reducer, initialAppState);
    const {group, bookableIndex, bookables, showDetails, isLoading, error} = state;

    const bookablesInGroup: Bookable[] = bookables.filter( b => b.group === group );
    const groups = [...new Set(bookables.map(b => b.group))];
    const bookable = bookablesInGroup.length === 0 || bookableIndex >= bookablesInGroup.length ? null : bookablesInGroup[bookableIndex];

    const timerRef = useRef<number | undefined>(undefined);
    // Call useRef and assign the ref to the nextButtonRef variable
    const nextButtonRef = useRef<HTMLButtonElement>(null);

    useEffect( () => {
        // Dispatch an action for the start of the data fetching
        dispatch({type: "FETCH_BOOKABLES_REQUEST"});

        getData<Bookable[]>("http://localhost:3001/bookables")
            .then(bookables => dispatch({
                type: "FETCH_BOOKABLES_SUCCESS",
                payload: bookables
            }))
            .catch(error => dispatch({
                type: "FETCH_BOOKABLES_ERROR",
                payload: error.message
            }));

        getData<string[]>("http://localhost:3001/days")
            .then(days => setDays(days))
            .catch(error => dispatch({
                type: "FETCH_BOOKABLES_ERROR",
                payload: error.message
            }));

        getData<string[]>("http://localhost:3001/sessions")
            .then(sessions => setSessions(sessions))
            .catch(error => dispatch({
                type: "FETCH_BOOKABLES_ERROR",
                payload: error.message
            }));

    }, []);

    useEffect( () => {
        timerRef.current = window.setInterval( () => {
            dispatch({ type: "NEXT_BOOKABLE"});
        }, 3000);

        return stopPresentation;
    }, []);

    function stopPresentation() {
        if (timerRef.current) {
            window.clearInterval(timerRef.current);
        }
    }

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
        nextButtonRef.current?.focus();
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

    if (error) {
        return (
            <p>{error}</p>
        );
    }

    if (isLoading) {
        return (
            <p><Spinner /> Loading bookables...</p>
        );
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
                    <button className="btn" onClick={nextBookable} ref={nextButtonRef} autoFocus>
                        <FaArrowRight />
                        <span>Next</span>
                    </button>
                </p>
            </div>

            { days && days.length !== 0 && sessions && sessions.length !== 0 && bookable && (
                <div className="bookable-details">
                    <div className="item">
                        <div className="item-header">
                            <h2>{bookable.title}</h2>
                            <span className="controls">
                                <label>
                                    <input type="checkbox" checked={showDetails} onChange={ () => toggleDetails() } />
                                    Show Details
                                </label>
                                <button className="btn" onClick={stopPresentation}>
                                    Stop
                                </button>
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