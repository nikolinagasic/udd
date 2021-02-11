import React, {useState} from "react";
import {Button, ButtonGroup, Form} from "react-bootstrap";
import Select from "react-select";

export default function PlagiarismComplaint() {

    const myBooks = [
        { value: "1", label: "Something is behind you" },
        { value: "2", label: "Can't stop this feeling" },
        { value: "3", label: "Paper" }
    ];

    const writersBooks = [
        { value: "1", label: "Get out, Mike Collins" },
        { value: "2", label: "Parachute, Jennifer Oak" },
        { value: "3", label: "Best day ever, Delilah Garner" }
    ];

    const [complaintStatus, setComplaintStatus] = useState("");

    return (
        <div className="bg-dark p-5">
            <div className="bg-dark p-5 border border-light text-left">
                <h2 className="text-left text-light mb-4">
                    Send plagiarism complaint
                </h2>
                <Form className="mt-5 mb-5 w-25">
                    <Form.Group controlId="my_books" className="text-left">
                        <Form.Label className="text-light">Select your book</Form.Label>
                        <Select className="" options={myBooks} isDisabled={complaintStatus}/>
                    </Form.Group>
                    <Form.Group controlId="writers_books" className="text-left">
                        <Form.Label className="text-light">Select writer and book</Form.Label>
                        <Select className="" options={writersBooks} isDisabled={complaintStatus}/>
                    </Form.Group>
                    <Button variant="outline-danger" className="mt-3"
                            onClick={() => setComplaintStatus("WAITING_MAIN_EDITOR")} disabled={complaintStatus}>
                        SEND COMPLAINT
                    </Button>
                </Form>
                {   complaintStatus === "WAITING_MAIN_EDITOR" &&
                    <>
                        <h5 className="text-danger text-left">
                            Waiting for main editor...
                        </h5>
                    </>
                }
                {   complaintStatus === "WAITING_EDITORS" &&
                <>
                    <h5 className="text-warning text-left">
                        Waiting editors to add notes...
                    </h5>
                </>
                }
                {   complaintStatus === "WAITING_REPLACEMENT" &&
                <>
                    <h5 className="text-info text-left">
                        Deadline for adding notes expired... Waiting main editor to choose new editors.
                    </h5>
                </>
                }
                {   complaintStatus === "WAITING_BOARD" &&
                <>
                    <h5 className="text-primary text-left">
                        Waiting board to decide ...
                    </h5>
                </>
                }
                {   complaintStatus === "DONE" &&
                <>
                    <h5 className="text-primary text-left">
                        Not plagiat, sorry ...
                    </h5>
                </>
                }

                {/*Delete testing elements from here*/}
                <div className="row ml-1 mt-5">
                    <ButtonGroup>
                        <Button variant="outline-danger" className="mt-3"
                                onClick={() => setComplaintStatus("WAITING_EDITORS")}>
                            MAIN EDITOR CHOOSE
                        </Button>
                        <Button variant="outline-warning" className="mt-3"
                                onClick={() => setComplaintStatus("WAITING_BOARD")}>
                            EDITORS ADDED NOTES
                        </Button>
                        <Button variant="outline-light" className="mt-3"
                                onClick={() => setComplaintStatus("WAITING_REPLACEMENT")}>
                            DEADLINE EXPIRED
                        </Button>
                        <Button variant="outline-info" className="mt-3"
                                onClick={() => setComplaintStatus("WAITING_EDITORS")}>
                            MAIN EDITOR REPLACED
                        </Button>
                        <Button variant="outline-success" className="mt-3"
                                onClick={() => setComplaintStatus("DONE")}>
                            BOARD DECIDED
                        </Button>
                        <Button variant="outline-primary" className="mt-3"
                                onClick={() => setComplaintStatus("WAITING_MAIN_EDITOR")}>
                            BOARD CAN'T DECIDE
                        </Button>
                    </ButtonGroup>
                </div>
                {/*End of test elements*/}
            </div>
        </div>
    );
}