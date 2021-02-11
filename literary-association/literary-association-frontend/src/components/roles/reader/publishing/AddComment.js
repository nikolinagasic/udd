import {Modal} from "react-bootstrap";
import React from "react";
import CustomForm from "../../../core/CustomForm";

export default function AddComment(props) {

    const handleClose = () => {
        props.onHide();
    };

    const submittedForm = () => {
        props.onHide();
    }

    return (
        <Modal centered show={props.show} onHide={handleClose} backdrop="static" className="explanation">
            <Modal.Body className="bg-light p-5">
                <h3>Comment</h3>
                <CustomForm formFieldsDTO={props.commentForm}
                            loggedIn={props.loggedIn}
                            submittedForm={submittedForm}
                            isFileForm={false}
                            checkFlags={false}
                            buttons={
                                [
                                    {
                                        variant: "outline-success",
                                        text: "ADD COMMENT"
                                    }
                                ]
                            }/>
            </Modal.Body>
        </Modal>
    );
}