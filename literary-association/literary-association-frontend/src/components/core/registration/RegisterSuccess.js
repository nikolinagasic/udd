import React from "react";
import {Form} from "react-bootstrap";
import Select from "react-select";

export default function RegisterSuccess() {
    return (
        <div className="col-4 content bg-dark p-1">
            <div className="m-5 custom-form border-light border pb-5">
                <h4 className="text-justify text-success p-3 mt-5">
                    Your registration has been successfull. We have sent you an email.
                </h4>
            </div>
        </div>
    );
}