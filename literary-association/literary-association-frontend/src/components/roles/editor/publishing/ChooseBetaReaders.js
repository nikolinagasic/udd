import React from "react";
import {Modal} from "react-bootstrap";
import CustomForm from "../../../core/CustomForm";

export default function ChooseBetaReaders(props) {

    const handleClose = () => {
        props.onHide();
    };

    const submittedForm = () => {
        props.onHide();
    }

    return (
        <Modal centered show={props.show} onHide={handleClose} backdrop="static" className="choose-beta-readers">
            <Modal.Body className="p-5">
                <CustomForm formFieldsDTO={props.betaReadersForm}
                            loggedIn={props.loggedIn}
                            submittedForm={submittedForm}
                            isFileForm={false}
                            checkFlags={false}
                            buttons={
                                [
                                    {
                                        variant: "outline-success",
                                        text: "DONE"
                                    }
                                ]
                            }/>
            </Modal.Body>
        </Modal>
    );
}