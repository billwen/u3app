import React, {useState} from "react";
import {FaArrowRight} from "react-icons/fa";

export type Bookable = {
    id: number;
    group: string;
    title: string;
    notes: string;
    sessions: number[];
    days: number[];
};

const bookables: Bookable[] = [
    {
        id: 1,
        group: "Rooms",
        title: "Meeting Room",
        notes: "The one with the big table and interactive screen. Seats 12. See Colin if you need the tea and coffee trolley.",
        sessions: [
            1,
            2,
            3
        ],
        days: [
            1,
            2,
            3,
            4,
            5
        ]
    },
    {
        id: 2,
        group: "Rooms",
        title: "Lecture Hall",
        notes: "For more formal 'sage-on-the-stage' presentations. Seats 100. See Sandra for help with AV setup.",
        sessions: [
            1,
            3,
            4
        ],
        days: [
            0,
            1,
            2,
            3,
            4
        ]
    },
    {
        id: 3,
        group: "Rooms",
        title: "Games Room",
        notes: "Table tennis, table football, pinball! There's also a selection of board games. Please tidy up!",
        sessions: [
            0,
            2,
            4
        ],
        days: [
            0,
            2,
            3,
            4,
            5,
            6
        ]
    },
    {
        id: 4,
        group: "Rooms",
        title: "Lounge",
        notes: "A relaxing place to hang out. Ideal for bean bag wranglers and sofa surfers. Help yourself to a beer after hours.",
        sessions: [
            0,
            1,
            2,
            3,
            4
        ],
        days: [
            0,
            1,
            2,
            3,
            4,
            5,
            6
        ]
    },
    {
        id: 5,
        group: "Kit",
        title: "Projector",
        notes: "Portable but powerful. Keep it with the case. Be careful, it gets quite hot after a while!",
        sessions: [
            1,
            2,
            3,
            4
        ],
        days: [
            0,
            2,
            3,
            4,
            5
        ]
    }
];

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