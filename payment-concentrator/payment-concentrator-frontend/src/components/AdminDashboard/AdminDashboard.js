import React, {useEffect, useState} from "react";
import {ButtonGroup, ToggleButton} from "react-bootstrap";
import SubscriptionRequestsService from "../../services/SubscriptionRequestsService";
import SubscriptionRequests from "../SubsciptionRequests/SubscriptionRequests";
import PaymentMethodsOverview from "../PaymentMethodsOverview/PaymentMethodsOverview";
import PaymentMethodsService from "../../services/PaymentMethodsService";
import "./AdminDashboard.css";
import Users from "../Users/Users";
import Transactions from "../Transactions/Transactions";

export default function AdminDashboard() {
    const [radioValue, setRadioValue] = useState("1");
    const [requests, setRequests] = useState([]);
    const [paymentMethods, setPaymentMethods] = useState([]);

    useEffect(() => {
        SubscriptionRequestsService.getAll().then((data) => setRequests(data));
        PaymentMethodsService.getAll().then((data) => setPaymentMethods(data));
    }, [])

    const approveOnClickHandler = (requestId) => {
        SubscriptionRequestsService.approve(requestId).then(() => {
            SubscriptionRequestsService.getAll().then((data) => setRequests(data));
        });
    }

    const declineOnClickHandler = (requestId) => {
        SubscriptionRequestsService.decline(requestId).then(() => {
            SubscriptionRequestsService.getAll().then((data) => setRequests(data));
        });
    }

    const createPaymentMethod = (name) => {
        PaymentMethodsService.createPaymentMethod(name).then(() => {
            PaymentMethodsService.getAll().then((data) => setPaymentMethods(data));
        })
    }

    const deletePaymentMethod = (methodId) => {
        PaymentMethodsService.deletePaymentMethod(methodId).then(() => {
            PaymentMethodsService.getAll().then((data) => setPaymentMethods(data));
        });
    }

    return (
        <React.Fragment>
            <div className="Admin row row-cols-2 justify-content-center mt-5 w-100">
                <ButtonGroup toggle>
                    <ToggleButton
                        className="mr-sm-1"
                        type="radio"
                        variant={radioValue === "1" ? "primary" : "secondary"}
                        name="radio"
                        value="1"
                        checked={radioValue === "1"}
                        onChange={(e) => setRadioValue(e.currentTarget.value)}>
                        Subscription Requests
                    </ToggleButton>
                    <ToggleButton
                        className="mr-sm-1"
                        type="radio"
                        variant={radioValue === "2" ? "primary" : "secondary"}
                        name="radio" value="2"
                        checked={radioValue === "2"}
                        onChange={(e) => setRadioValue(e.currentTarget.value)}>
                        Payment methods
                    </ToggleButton>
                    <ToggleButton
                        className="mr-sm-1"
                        type="radio"
                        variant={radioValue === "3" ? "primary" : "secondary"}
                        name="radio" value="3"
                        checked={radioValue === "3"}
                        onChange={(e) => setRadioValue(e.currentTarget.value)}>
                        Transactions
                    </ToggleButton>
                    <ToggleButton
                        type="radio"
                        variant={radioValue === "4" ? "primary" : "secondary"}
                        name="radio" value="4"
                        checked={radioValue === "4"}
                        onChange={(e) => setRadioValue(e.currentTarget.value)}>
                        Users
                    </ToggleButton>
                </ButtonGroup>
            </div>
            { radioValue == 1 &&
                <SubscriptionRequests requests={requests} approve={approveOnClickHandler}
                                  decline={declineOnClickHandler}/>
            }
            { radioValue == 2 &&
                <PaymentMethodsOverview methods={paymentMethods} delete={deletePaymentMethod}
                                  create={createPaymentMethod}/>
            }
            { radioValue == 3 &&
                <Transactions/>
            }
            { radioValue == 4 &&
                <Users/>
            }
        </React.Fragment>);
}