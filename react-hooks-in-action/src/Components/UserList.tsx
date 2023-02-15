import React, {useState} from "react";
import db from "../db.json";
import {User} from "Types/domain";

const {users} : {users: User[]} = db;

const UserList = () => {
    const [selectedUser, setSelectedUser] = useState(0);

    return (
        <ul className="user items-list-nav">
            { users.map( (u, i) => (
                <li key={u.id} className={ selectedUser === i ? "selected" : undefined } >
                    <button className="btn" onClick={ () => setSelectedUser(i) }>{u.name}</button>
                </li>
            ))}
        </ul>
    );
};

export default UserList;