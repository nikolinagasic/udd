import {Button, Form, Modal} from "react-bootstrap";
import React from "react";

export default function AddNote(props) {

    const handleClose = () => {
        props.onHide();
    };

    return (
        <Modal centered show={props.show} onHide={handleClose} className="explanation">
            <Modal.Body className="bg-light p-5">
                <h3>Note</h3>
                <Form.Group controlId="note" className="text-left">
                    <Form.Control as="textarea" rows={5} placeholder="Enter note text"/>
                </Form.Group>
                <Button variant="success float-right">
                    ADD NOTE
                </Button>
            </Modal.Body>
        </Modal>
    );
}