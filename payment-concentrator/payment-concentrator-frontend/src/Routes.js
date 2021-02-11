import React from "react";
import {Redirect, Route, Switch} from "react-router";
import WelcomeCard from "./components/WelcomeCard/WelcomeCard";
import LoginComponent from "./components/Login/Login";
import PaymentMethodsComponent from "./components/PaymentMethods/PaymentMethods";
import AdminDashboard from "./components/AdminDashboard/AdminDashboard";
import ChoosePaymentMethod from "./components/Payment/ChoosePaymentMethod";
import UserDashboard from "./components/UserDashboard/UserDashboard";

export default function Routes(props) {
    return (<Switch>
        <Route component={WelcomeCard} path="/" exact/>
        <Route component={ChoosePaymentMethod} path="/payment-methods/:request_id/:token" exact/>
        <Route render={() => (<LoginComponent log={props.log}/>)} path="/login" exact/>
        <Route
            component={PaymentMethodsComponent}
            path="/payment-methods"
            exact
        />
        { (props.loggedIn !== null && props.roles === "ROLE_ADMIN") &&
            <Route component={AdminDashboard} path="/dashboard" exact/>
        }
        { (props.loggedIn !== null && props.roles === "ROLE_SUBSCRIBER") &&
            <Route component={UserDashboard} path="/dashboard" exact/>
        }
    </Switch>);
}