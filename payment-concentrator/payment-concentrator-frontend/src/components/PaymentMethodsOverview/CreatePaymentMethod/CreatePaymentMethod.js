import React, {useState} from "react";
import {Button, Col, Form, Row} from "react-bootstrap";
import "./CreatePaymentMethod.css";

export default function CreatePaymentMethod(props) {
    const [name, setName] = useState("");

    return (<div>
        <Row className="Create w-50 m-5">
            <Col>
                <Form className="p-2">
                    <Form.Group controlId="formBasicText" className="m-2 p-1">
                        <Form.Label className="Label">Create new payment method</Form.Label>
                        <Form.Control type="text" placeholder="Name" onChange={e => setName(e.target.value)}/>
                    </Form.Group>
                    <Button variant="success" onClick={() => props.create(name)}>
                        Submit
                    </Button>
                </Form>
            </Col>
        </Row>
    </div>);
}