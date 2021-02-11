import React from "react";
import {Form, Button} from "react-bootstrap";

export default function ResetPassword() {

    return (
        <div className="col-4 content bg-dark h-100 p-1">
            <div className="m-5 custom-form border-light border">
                <Form className="m-5 w-75">
                    <h3 className="text-left w-75 pb-3">Reset password</h3>
                    <Form.Group controlId="formBasicEmail" className="text-left w-75">
                        <Form.Label>Email address</Form.Label>
                        <Form.Control type="email" placeholder="Enter email" />
                    </Form.Group>
                    <Button variant="primary" type="submit" className="mt-3 w-75">
                        Send password reset email
                    </Button>
                    <br/>
                </Form>
            </div>
        </div>
    );
}