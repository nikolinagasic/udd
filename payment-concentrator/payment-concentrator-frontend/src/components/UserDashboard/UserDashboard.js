import React, {useState} from "react";
import {ButtonGroup, ToggleButton} from "react-bootstrap";
import SubscriptionRequests from "../SubsciptionRequests/SubscriptionRequests";
import PaymentMethodsOverview from "../PaymentMethodsOverview/PaymentMethodsOverview";
import Transactions from "../Transactions/Transactions";
import Users from "../Users/Users";
import UserDetails from "../Users/UserDetails";

export default function UserDashboard() {
    const [radioValue, setRadioValue] = useState("1");

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
                        Account Details
                    </ToggleButton>
                    <ToggleButton
                        className="mr-sm-1"
                        type="radio"
                        variant={radioValue === "2" ? "primary" : "secondary"}
                        name="radio" value="2"
                        checked={radioValue === "2"}
                        onChange={(e) => setRadioValue(e.currentTarget.value)}>
                        Transactions
                    </ToggleButton>
                </ButtonGroup>
            </div>
            { radioValue == 1 &&
                <UserDetails/>
            }
            { radioValue == 2 &&
                <Transactions/>
            }
        </React.Fragment>);
}