import React, {useState} from "react";
import {Navbar, Nav} from "react-bootstrap";
import {Link} from "react-router-dom";
import {USER_ROLES} from "../../Enums";
import Logout from "./Logout";

export default function NavigationBar(props){

    return (
        <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark" className="mb-5">
            <Navbar.Brand href="#home">Books & Beyond</Navbar.Brand>
            <Navbar.Toggle aria-controls="responsive-navbar-nav" />
            <Navbar.Collapse id="responsive-navbar-nav">
                <Nav className="mr-auto">
                    <Link className="nav-link" to="/home">Home</Link>
                    <Link className="nav-link" to="/books">Books</Link>
                    <Link className="nav-link" to="/pricing">Pricing</Link>
                </Nav>
                <Nav className="ml-auto">
                    {  props.loggedIn &&
                        <>
                        {   props.roles[0] === USER_ROLES.WRITER_MEMBERSHIP_REQUEST &&
                            <>
                                <Link className="nav-link" to="/submitted-work">
                                    Submitted work
                                </Link>
                            </>
                        }
                        {   props.roles[0] === USER_ROLES.WRITER &&
                            <>
                                <Link className="nav-link" to="/publish-book">
                                    Publish book
                                </Link>
                                <Link className="nav-link" to="/plagiarism-complaint">
                                    Plagiarism complaint
                                </Link>
                            </>
                        }
                        {   props.roles[0] === USER_ROLES.EDITOR &&
                            <>
                                <Link className="nav-link" to="/publish-requests">
                                    Publish requests
                                </Link>
                                <Link className="nav-link" to="/main-editor-complaints">
                                    Plagiarism complaints
                                </Link>
                                <Link className="nav-link" to="/notes-complaints">
                                    Comparation requests
                                </Link>
                            </>
                        }
                        {   props.roles[0] === USER_ROLES.READER &&
                            <>
                                <Link className="nav-link" to="/comments-scripts">
                                    Review scripts
                                </Link>
                            </>
                        }
                        {   props.roles[0] === USER_ROLES.LECTOR &&
                            <>
                                <Link className="nav-link" to="/corrections-scripts">
                                    Correction requests
                                </Link>
                            </>
                        }
                        {   props.roles[0] === USER_ROLES.BOARD_MEMBER &&
                            <>
                                <Link className="nav-link" to="/registration-requests">
                                    Registration requests
                                </Link>
                                <Link className="nav-link" to="/review-notes">
                                    Review notes
                                </Link>
                            </>
                        }
                            <Link className="nav-link" to="/home">
                                <Logout logout={props.logout}/>
                            </Link>
                        </>
                    }
                    { !props.loggedIn &&
                        <Link className="nav-link" to="/home">
                            Login
                        </Link>
                    }
                    <Link className="nav-link" to="/cart">
                        <span className="material-icons">shopping_cart</span>
                        <span style={{verticalAlign:"top"}}>({props.cartItemsLength})</span>
                    </Link>
                </Nav>
            </Navbar.Collapse>
        </Navbar>
    );
}