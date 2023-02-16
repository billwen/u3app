import React, {useEffect, useState} from "react";
import {User} from "Types/domain";
import Spinner from "Components/UI/Spinner";

const UserPicker = () => {
    const [users, setUsers] = useState<User[] | null>(null);

    useEffect( () => {
        fetch("http://localhost:3001/users")
            .then(resp => resp.json())
            .then(data => setUsers(data));
    }, []);

    if (users === null) {
        return (
            <Spinner />
        );
    }
    return (
        <select>
            <option value="">Users</option>
            { users.map( (u,i) => ( <option value={u.id} key={u.id}>{u.name}</option> ) ) }
        </select>
    );
};

export default UserPicker;