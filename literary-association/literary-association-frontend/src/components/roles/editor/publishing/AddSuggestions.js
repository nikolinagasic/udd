import {Modal} from "react-bootstrap";
import React from "react";
import CustomForm from "../../../core/CustomForm";

export default function AddSuggestions(props) {

    const handleClose = () => {
        props.onHide();
    };

    const submittedForm = () => {
        props.onHide();
    }

    return (
        <Modal centered show={props.show} onHide={handleClose} backdrop="static" className="explanation">
            <Modal.Body className="bg-light p-5">
                <h3>Suggestion</h3>
                <CustomForm formFieldsDTO={props.suggestionForm}
                            loggedIn={props.loggedIn}
                            submittedForm={submittedForm}
                            isFileForm={false}
                            checkFlags={false}
                            buttons={
                                [
                                    {
                                        variant: "outline-success",
                                        text: "SEND SUGGESTIONS"
                                    }
                                ]
                            }/>
            </Modal.Body>
        </Modal>
    );
}