import React from "react";
import {Button, Modal} from "react-bootstrap";

export default function Confirmation(props) {

    const handleClose = (confirmed) => {
        props.onHide(confirmed);
    };

    return (
        <Modal centered show={props.show} onHide={handleClose} className="confirmation">
            <Modal.Body className="bg-light text-center p-4">
                <h4>Are you sure?</h4>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={() => handleClose(false)} variant="primary" className="col-2">
                    No
                </Button>
                <Button onClick={() => handleClose(true)} variant="danger" className="col-2">
                    Yes
                </Button>
            </Modal.Footer>
        </Modal>
    );
}