import React, {useState} from "react";
import "./Login.scss";
import LoginIcon from "@material-ui/icons/AccountCircleSharp";
import PassIcon from "@material-ui/icons/LockSharp";
import {Redirect} from "react-router";
import {Alert} from "react-bootstrap";

export default function LoginComponent(props) {
    const [username, setUsername] = useState();
    const [password, setPassword] = useState();
    const [redirect, setRedirect] = useState(false);
    const [showAlert, setShowAlert] = useState(false);

    const usernameOnChangeHandler = (value) => {
        setUsername(value);
    }

    const passwordOnChangeHandler = (value) => {
        setPassword(value);
    }

    const a = (value, role) => {
        props.log(value, role);
    }

    const login = () => {
        fetch("https://localhost:8081/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username : username,
                password : password
            }),
        })
            .then(response => response.json())
            .then(data => {
                a(data.accessToken, data.roles[0]);
                setRedirect(true);
            })
            .catch((error) => {
                setShowAlert(true);
                console.error('ERROR:', error);
            });
    }

    if (redirect) {
        return <Redirect to="/dashboard"/>
    }

    return (

        <div className="Login" style={{height:"400px"}}>
            <Alert style={{
                width: "350px",
                height: "fit-content",
                marginBottom: "50px"}} className="Alert" variant={"danger"} show={showAlert} transition={true}>
                Wrong username or password
            </Alert>
            <div className="input-container">
                <input type="text" placeholder="Username" onChange={e => usernameOnChangeHandler(e.target.value)}/>
                <i>
                    <LoginIcon/>
                </i>
            </div>
            <div className="input-container">
                <input type="password" placeholder="Password" onChange={e => passwordOnChangeHandler(e.target.value)}/>
                <i>
                    <PassIcon/>
                </i>
            </div>
            <button type="submit" className="ButtonLogin" onClick={() => login()}>
                Log In
            </button>
        </div>
    );
}
