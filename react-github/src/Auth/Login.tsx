import React from "react";

const Login = () => {
    const client_id = process.env.REACT_APP_CLIENT_ID!;
    const client_secret = process.env.REACT_APP_CLIENT_SECRET!;
    const github_url_authorize = `https://github.com/login/oauth/authorize?client_id=${client_id}&scope=user%20read:org%20public_repo%20admin:enterprise&state=abc`;

    console.log(`client id : ${client_id}, client secret : ${client_secret}`);

    const onLogin = () => {
        window.location.href = github_url_authorize;
    }

    return (
        <div>
            <h1>Login</h1>
            <button type="button" onClick={onLogin}>Github</button>
        </div>
    );
}

export default Login;