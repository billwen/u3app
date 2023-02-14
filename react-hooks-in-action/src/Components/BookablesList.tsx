import React from "react";

import { bookables } from "../../db.json";

/*
{
      "id": 1,
      "group": "Rooms",
      "title": "Meeting Room",
      "notes": "The one with the big table and interactive screen. Seats 12. See Colin if you need the tea and coffee trolley.",
      "sessions": [
        1,
        2,
        3
      ],
      "days": [
        1,
        2,
        3,
        4,
        5
      ]
    }
 */
export type Bookable = {
    id: number;
    group: string;
    title: string;
    notes: string;
    sessions: number[];
    days: number[];
};

const BookablesList = () => {

    const group = "Rooms";

    const bookablesInGroup: Bookable[] = bookables.filter( b => b.group === group );

    const bookableIndex: number = 1;

    return (
        <ul className="bookables items-list-nav">
            { bookablesInGroup.map( (b, i) => (
                <li key={b.id} className={ i === bookableIndex ? "selected" : null }>
                    <button className="btn">{b.title}</button>
                </li>
                )
            ) }
        </ul>
    );
};

export default BookablesList;