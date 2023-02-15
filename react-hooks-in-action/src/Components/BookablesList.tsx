import React, {useState} from "react";
import {FaArrowRight} from "react-icons/fa";
import {Bookable} from "Types/domain";

import db from "../db.json";

const {bookables} : {bookables: Bookable[]} = db;

const BookablesList = () => {

    const [group, setGroup] = useState("Kit");

    const bookablesInGroup: Bookable[] = bookables.filter( b => b.group === group );

    const [bookableIndex, setBookableIndex] = useState(0);

    const groups = [...new Set(bookables.map(b => b.group))];

    const nextBookable = () => {
        setBookableIndex( i => (i + 1) % bookablesInGroup.length)
    }

    return (
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

    );
};

export default BookablesList;