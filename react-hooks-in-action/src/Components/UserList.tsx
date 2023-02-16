import React, {useEffect, useState} from "react";
import {User} from "Types/domain";
import Spinner from "Components/UI/Spinner";

const UserList = () => {
    const [users, setUsers] = useState<User[] | null>(null);
    const [selectedUserId, setSelectedUserId] = useState(0);
    const selectedUser = users == null ? null : users[selectedUserId];

    useEffect( () => {
        fetch("http://localhost:3001/users")
            .then(resp => resp.json())
            .then(data => setUsers(data));
    }, []);

    if (users === null || selectedUser === null) {
        return (
            <Spinner />
        );
    }
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