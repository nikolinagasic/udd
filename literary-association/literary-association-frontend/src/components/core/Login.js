import React, {useState} from "react";
import {Form, Button} from "react-bootstrap";
import {Link} from "react-router-dom";

export default function Login(props) {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [error, setError] = useState(false);

    const usernameOnChangeHandler = (value) => {
        setUsername(value);
    }

    const passwordOnChangeHandler = (value) => {
        setPassword(value);
    }

    const login = () => {
        fetch("https://localhost:8080/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username: username,
                password: password
            }),
        })
            .then(response => response.json())
            .then(data => {
                if (data.message) {
                    alert(data.message);
                    setError(true);
                    return;
                }
                props.login(data.accessToken, data.roles);

            })
            .catch((error) => {
                alert('Wrong credentials. Please check username and password, or activate your account.');
                setError(true);
            });
    }

    return (
        <div className="m-5 custom-form border-light border pb-5">
            <Form className="mt-5 mb-5 w-50">
                <h3 className="text-left pb-3">Login</h3>
                {error &&
                <h6 className="text-danger text-left pb-1">Invalid user credentials!</h6>
                }
                <Form.Group controlId="username" className="text-left">
                    <Form.Label>Username</Form.Label>
                    <Form.Control type="text" className={error ? "border border-danger" : ""}
                                  placeholder="Enter username"
                                  onChange={(e) => usernameOnChangeHandler(e.target.value)}/>
                </Form.Group>
                <Form.Group controlId="password" className="text-left">
                    <Form.Label>Password</Form.Label>
                    <Form.Control type="password" className={error ? "border border-danger" : ""} placeholder="Password"
                                  onChange={(e) => passwordOnChangeHandler(e.target.value)}/>
                </Form.Group>
                <Form.Text className="text-muted text-left">
                    <Link to="/reset-password">Forgot your password?</Link>
                </Form.Text>
                <Form.Text className="text-muted text-left">
                    <Link to="/register">Don't have an account? Sign up.</Link>
                </Form.Text>
                <Button variant="primary" className="float-right w-100 mt-3" onClick={() => login()}>
                    Login
                </Button>
            </Form>
        </div>
    );
}