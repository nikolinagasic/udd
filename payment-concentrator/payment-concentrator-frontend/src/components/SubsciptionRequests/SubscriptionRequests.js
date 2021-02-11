import React, {useState} from "react";
import {Button, Table, Modal} from "react-bootstrap";
import "./SubscriptionRequests.css";

export default function SubscriptionRequests(props) {
    const [show, setShow] = useState(false);
    const [selectedRequestMethods, setSelectedRequestMethods] = useState([]);

    const handleClose = () => setShow(false);
    const handleShow = (methods) => {
        setSelectedRequestMethods(methods);
        setShow(true);
    }

    return (<div className="Center mt-5 w-100">
        <Table striped bordered hover variant="dark" responsive
               className="w-75">
            <thead>
            <tr>
                <th>#</th>
                <th>Organization name</th>
                <th>Organization email</th>
                <th>Organization description</th>
                <th>Request methods</th>
                <th>Approve</th>
                <th>Decline</th>
            </tr>
            </thead>
            <tbody>
            {props.requests.map((request, index) => {
                return <tr key={request.id}>
                    <th>{index + 1}</th>
                    <th>{request.organizationName}</th>
                    <th>{request.organizationEmail}</th>
                    <th>{request.organizationDescription}</th>
                    <th><Button variant="primary" onClick={() => handleShow(request.paymentMethods)}>Show</Button></th>
                    <th><Button variant="success" onClick={() => props.approve(request.id)}>Approve</Button></th>
                    <th><Button variant="danger" onClick={() => props.decline(request.id)}>Decline</Button></th>
                </tr>
            })}
            </tbody>
        </Table>
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Requested payment methods</Modal.Title>
            </Modal.Header>
            <Modal.Body>{selectedRequestMethods.map((method, index) => {
                return <h3>{(index + 1) + " " + method.name}</h3>
            })}</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Close
                </Button>
            </Modal.Footer>
        </Modal>
    </div>);
}