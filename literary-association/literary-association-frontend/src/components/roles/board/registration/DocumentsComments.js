import React, {useEffect, useState} from "react";
import {Button, Form, Modal, Table} from "react-bootstrap";
import PreviewPDF from "../../../core/modals/PreviewPDF";
import CustomForm from "../../../core/CustomForm";

export default function DocumentsComments(props) {

    const handleClose = () => {
        props.onHide();
        if (props.hasCallback) {
            props.callBack();
        }
    };

    const [commentForm, setCommentForm] = useState(null);

    useEffect(() => {
        fetch("https://localhost:8080/api/registration/leave-comment-form/" + props.selectedRequest.processInstanceId, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + JSON.parse(localStorage.getItem("token")),
                "Content-Type": "application/json",
            },
        })
            .then(response => response.json())
            .then(data => {
                setCommentForm(data);
            })
            .catch((error) => {
                console.log(error)
            });
    }, []);

    return (
        <Modal centered show={props.show} onHide={handleClose} className="documents-comments">
            <Modal.Body className="bg-light">
                {commentForm &&
                <CustomForm formFieldsDTO={commentForm}
                            loggedIn={JSON.parse(localStorage.getItem("token"))}
                            submittedForm={handleClose}
                            isFileForm={false}
                            checkFlags={false}
                            buttons={
                                [
                                    {
                                        variant: "success",
                                        text: "Leave Comment"
                                    }
                                ]
                            }/>
                }
            </Modal.Body>
        </Modal>
    );
}