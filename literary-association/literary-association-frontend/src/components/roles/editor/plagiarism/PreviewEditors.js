import React, {useState} from "react";
import {Button, Form, Modal, Table} from "react-bootstrap";
import Select from "react-select";

export default function PreviewEditors(props) {

    const [note, setNote] = useState(null);

    const handleClose = () => {
        props.onHide();
    };

    const editors = [
        { value: "1", label: "Luke Bowman" },
        { value: "2", label: "Ema Case" },
        { value: "3", label: "Uma Lopez" }
    ];

    return (
        <Modal centered show={props.show} onHide={handleClose} className="preview-comments">
            <Modal.Body className="bg-light">
                <div className="bg-light p-5">
                    {   props.status === "WAITING_MAIN_EDITOR" &&
                        <Form className="mb-5 w-50">
                            <Form.Group controlId="my_books" className="text-left">
                                <Form.Label className="">Select editors</Form.Label>
                                <Select className="" options={editors}/>
                            </Form.Group>
                            <Button variant="outline-success" className=""
                                    onClick={() => props.setStatus("WAITING_EDITORS")}>
                                ADD EDITORS
                            </Button>
                        </Form>
                    }
                        {props.status !== "WAITING_MAIN_EDITOR" &&
                            <h5 className="text-danger mb-5">Notes deadline : 15.1.2021.</h5>
                        }
                            <Table striped bordered hover variant="">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Name</th>
                                        <th>Surname</th>
                                        <th></th>
                                    </tr>
                                </thead>
                            <tbody>
                            {props.status !== "WAITING_MAIN_EDITOR" &&
                            <>
                                 <tr>
                                    <td>1</td>
                                    <td>Luke</td>
                                    <td>Bowman</td>
                                    <td className="text-center" style={{verticalAlign: "middle"}}>
                                {(props.status === "WAITING_REPLACEMENT" && !note) &&
                                    <Button variant="danger" className="" onClick={() => props.setStatus("WAITING_EDITORS")}>
                                    REPLACE EDITOR
                                    </Button>
                                }
                                    </td>
                                </tr>
                                    <tr>
                                        <td>2</td>
                                        <td>Uma</td>
                                        <td>Lopez</td>
                                        <td className="text-center" style={{verticalAlign: "middle"}}>
                                        </td>
                                </tr>
                            </>
                            }
                            </tbody>
                    </Table>
                </div>
            </Modal.Body>
        </Modal>
    );
}