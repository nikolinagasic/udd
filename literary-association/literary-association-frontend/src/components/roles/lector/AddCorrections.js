import {Modal} from "react-bootstrap";
import React from "react";
import CustomForm from "../../core/CustomForm";

export default function AddCorrections(props) {

    const handleClose = () => {
        props.onHide();
    };

    const submittedForm = () => {
        props.onHide();
    }

    return (
        <Modal centered show={props.show} onHide={handleClose} backdrop="static" className="explanation">
            <Modal.Body className="bg-light p-5">
                <h3>Corrections</h3>
                <CustomForm formFieldsDTO={props.correctionForm}
                            loggedIn={props.loggedIn}
                            submittedForm={submittedForm}
                            isFileForm={false}
                            checkFlags={false}
                            buttons={
                                [
                                    {
                                        variant: "outline-success",
                                        text: "ADD CORRECTIONS"
                                    }
                                ]
                            }/>
            </Modal.Body>
        </Modal>
    );
}