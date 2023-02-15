import React, {useState} from "react";
import db from "../db.json";
import {User} from "Types/domain";

const {users} : {users: User[]} = db;

const UserList = () => {
    const [selectedUserId, setSelectedUserId] = useState(0);
    const selectedUser = users[selectedUserId];
    console.log(selectedUser);

    return (
        <>
            <ul className="items-list-nav">
                { users.map( (u, i) => (
                    <li key={u.id} className={ selectedUserId === i ? "selected" : undefined } >
                        <button className="btn" onClick={ () => setSelectedUserId(i) }>{u.name}</button>
                    </li>
                ))}
            </ul>

            <div className="users-page">
                <div className="item">
                    <div className="item-header">
                        <h2>{selectedUser.name}</h2>
                    </div>

                    <div className="item-details">
                        <p>{ selectedUser.title }</p>
                        <p>{selectedUser.notes}</p>
                    </div>
                </div>
            </div>
        </>

    );
};

export default UserList;