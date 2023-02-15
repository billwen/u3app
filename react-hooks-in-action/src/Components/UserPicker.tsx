import React from "react";
import db from "../db.json"
import {User} from "../Types/domain";

const { users } : {users: User[]} = db;

const UserPicker = () => {
    return (
        <select>
            <option value="#">Users</option>
            { users.map( (u,i) => ( <option value={u.id} key={u.id}>{u.name}</option> ) ) }
        </select>
    );
};

export default UserPicker;