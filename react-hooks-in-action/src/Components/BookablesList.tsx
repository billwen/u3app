import React, {useState} from "react";
import {FaArrowRight} from "react-icons/fa";
import {Bookable} from "Types/domain";

import db from "../db.json";

const {bookables, days, sessions} : {bookables: Bookable[], days: string[], sessions: string[]} = db;

const BookablesList = () => {

    const [group, setGroup] = useState("Kit");

    const bookablesInGroup: Bookable[] = bookables.filter( b => b.group === group );

    const [bookableIndex, setBookableIndex] = useState(0);

    const groups = [...new Set(bookables.map(b => b.group))];

    const bookable = bookablesInGroup[bookableIndex];

    const [showDetails, setShowDetails] = useState(false);

    const nextBookable = () => {
        setBookableIndex( i => (i + 1) % bookablesInGroup.length)
    }

    return (
        <>
            <div>
                <select value={group} onChange={ (e) => setGroup(e.target.value)}>
                    {
                        groups.map( g => <option value={g} key={g}>{g}</option>)
                    }
                </select>

                <ul className="bookables items-list-nav">
                    { bookablesInGroup.map( (b, i) => (
                            <li key={b.id} className={ i === bookableIndex ? "selected" : undefined }>
                                <button className="btn" onClick={ () => setBookableIndex(i) }>{b.title}</button>
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
                                    <input type="checkbox" checked={showDetails} onChange={ () => setShowDetails( has => !has)} />
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