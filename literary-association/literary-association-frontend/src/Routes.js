import React from "react";
import {Route, Switch} from "react-router-dom";
import Home from "./components/core/Home";
import Register from "./components/core/registration/Register";
import Pricing from "./components/membership/Pricing";
import ResetPassword from "./components/core/password/ResetPassword";
import RegisterSuccess from "./components/core/registration/RegisterSuccess";
import RegisterReader from "./components/core/registration/RegisterReader";
import RegisterBetaReader from "./components/core/registration/RegisterBetaReader";
import Books from "./components/books/Books";
import SubmittedWork from "./components/roles/writer/registration/SubmittedWork";
import RegistrationRequests from "./components/roles/board/registration/RegistrationRequests";
import PublishBook from "./components/roles/writer/publishing/PublishBook";
import PublishRequests from "./components/roles/editor/publishing/PublishRequests";
import CorrectionsScripts from "./components/roles/lector/CorrectionsScripts";
import CommentsScripts from "./components/roles/reader/publishing/CommentsScripts";
import PlagiarismComplaint from "./components/roles/writer/plagiarism/PlagiarismComplaint";
import MainEditorComplaints from "./components/roles/editor/plagiarism/MainEditorComplaints";
import NotesComplaints from "./components/roles/editor/plagiarism/NotesComplaints";
import ReviewNotes from "./components/roles/board/plagiarism/ReviewNotes";
import {USER_ROLES} from "./Enums";
import Cart from "./components/payment/Cart";
import Success from "./components/payment/Success";
import Failed from "./components/payment/Failed";
import Error from "./components/payment/Error";
import ActivateAccount from "./components/core/registration/ActivateAccount";

export default function Routes(props) {

    return (
        <Switch>
            <Route exact path="/">
                <Home loggedIn={props.loggedIn} login={props.login} logout={props.logout}/>
            </Route>
            <Route exact path="/home">
                <Home loggedIn={props.loggedIn} login={props.login} logout={props.logout}/>
            </Route>
            <Route exact path="/pricing">
                <Pricing loggedIn={props.loggedIn} roles={props.roles}/>
            </Route>
            <Route exact path="/books">
                <Books loggedIn={props.loggedIn}
                       cartItems={props.cartItems}
                       setCartItems={props.setCartItems}/>
            </Route>
            <Route component={Success} path="/success/:request_id?" exact/>
            <Route component={Failed} path="/failed/:request_id" exact/>
            <Route component={Error} path="/error/:request_id" exact/>
            <Route exact path="/cart">
                <Cart loggedIn={props.loggedIn}
                      cartItems={props.cartItems}
                      setCartItems={props.setCartItems}/>
            </Route>
            {   !props.loggedIn &&
                <>
                    <Route exact path="/reset-password">
                        <ResetPassword/>
                    </Route>
                    <Route exact path="/register">
                        <Register/>
                    </Route>
                    <Route exact path="/register-success">
                        <RegisterSuccess/>
                    </Route>
                    <Route exact path="/register-reader">
                        <RegisterReader/>
                    </Route>
                    <Route exact path="/register-beta-reader">
                        <RegisterBetaReader/>
                    </Route>
                    <Route path="/activate-account">
                        <ActivateAccount/>
                    </Route>
                </>
            }
            { props.loggedIn &&
                <>
                {   props.roles[0] === USER_ROLES.WRITER_MEMBERSHIP_REQUEST &&
                    <Route exact path="/submitted-work">
                        <SubmittedWork/>
                    </Route>
                }
                {  props.roles[0] === USER_ROLES.WRITER &&
                    <>
                        <Route exact path="/publish-book">
                            <PublishBook loggedIn={props.loggedIn}/>
                        </Route>
                        <Route exact path="/plagiarism-complaint">
                            <PlagiarismComplaint/>
                        </Route>
                    </>
                }
                {   props.roles[0] === USER_ROLES.READER &&
                    <Route exact path="/comments-scripts">
                        <CommentsScripts loggedIn={props.loggedIn}/>
                    </Route>
                }
                {   props.roles[0] === USER_ROLES.BOARD_MEMBER &&
                    <>
                        <Route exact path="/registration-requests">
                            <RegistrationRequests/>
                        </Route>
                        <Route exact path="/review-notes">
                            <ReviewNotes/>
                        </Route>
                    </>
                }
                {   props.roles[0] === USER_ROLES.LECTOR &&
                    <Route exact path="/corrections-scripts">
                        <CorrectionsScripts loggedIn={props.loggedIn}/>
                    </Route>
                }
                {   props.roles[0] === USER_ROLES.EDITOR &&
                    <>
                        <Route exact path="/publish-requests">
                            <PublishRequests loggedIn={props.loggedIn}/>
                        </Route>
                        <Route exact path="/main-editor-complaints">
                            <MainEditorComplaints/>
                        </Route>
                        <Route exact path="/notes-complaints">
                            <NotesComplaints/>
                        </Route>
                    </>
                }
                </>
            }
            <Route>
                <Home loggedIn={props.loggedIn} login={props.login} logout={props.logout}/>
            </Route>
        </Switch>
    );
}