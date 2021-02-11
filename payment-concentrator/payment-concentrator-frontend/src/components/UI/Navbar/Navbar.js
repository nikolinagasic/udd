import React from "react";
import {Nav, Navbar, Button} from "react-bootstrap";
import LoginIcon from "@material-ui/icons/AccountCircleSharp";
import {Link} from "react-router-dom";
import {Redirect} from "react-router";

export default function navbar(props) {
    const logout = () => {
        props.log(null, null);
        window.location.replace("https://localhost:3001/login");
    }

    return (
        <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark" className="mb-5">
            <Navbar.Brand>Payment Concentrator</Navbar.Brand>
            <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
            {(props.loggedIn === null)?
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="w-100">
                        <Link className="nav-link" to="/">Home</Link>
                        <Link className="nav-link" to="/payment-methods">Payment methods overview</Link>
                    </Nav>
                    <Nav>
                        <Link className="nav-link" to="/">Register</Link>
                        <Link className="nav-link float-right d-flex" to="/login"><LoginIcon/>Login</Link>
                    </Nav>
                </Navbar.Collapse>
                :
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="w-100">
                    </Nav>
                    <Button variant="outline-primary" className="nav-link float-right d-flex" onClick={() => logout()}><LoginIcon/>Logout</Button>
                </Navbar.Collapse>
            }
        </Navbar>
    );
}
