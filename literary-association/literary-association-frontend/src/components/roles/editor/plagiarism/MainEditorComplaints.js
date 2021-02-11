import React, {useState} from "react";
import {Button, ButtonGroup, Form, Table} from "react-bootstrap";
import PreviewEditors from "./PreviewEditors";

export default function MainEditorComplaints() {

    const [status, setStatus] = useState("WAITING_MAIN_EDITOR");
    const [showPreviewEditors, setShowPreviewEditors] = useState(false);

    const handleShowPreviewEditors = () => setShowPreviewEditors(true);
    const handleClosePreviewEditors = () => setShowPreviewEditors(false);

    const renderStatus = () => {
        switch (status) {
            case "WAITING_MAIN_EDITOR" : return "CHOOSE EDITORS";
            case "WAITING_EDITORS" : return "DEADLINE : 15.1.2021.";
            case "WAITING_REPLACEMENT" : return "REPLACE EDITORS";
            case "WAITING_BOARD" : return "BOARD IS DECIDING";
        }
    }

    return (
        <div className="bg-dark p-5">
            <PreviewEditors show={showPreviewEditors} onHide={() => handleClosePreviewEditors()} status={status} setStatus={setStatus}/>
            <div className="bg-dark p-5 border border-light text-left">
                <h2 className="text-left text-light mb-4">
                    Plagiarism requests
                </h2>
                <Table striped bordered hover variant="dark">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Original</th>
                        <th>Potential plagiat</th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>1</td>
                        <td>Something is behind you, Neil Gaiman</td>
                        <td>Get out, Mike Collins</td>
                        <td className="text-info font-weight-bold text-center" style={{verticalAlign:"middle"}}>
                            {renderStatus(status)}
                        </td>
                        <td className="text-center" style={{verticalAlign:"middle"}}>
                            <Button variant="outline-danger" className="mt-3"
                                    onClick={() => handleShowPreviewEditors()}>
                                PREVIEW EDITORS
                            </Button>
                        </td>
                    </tr>
                    </tbody>
                </Table>
                {/*Delete testing elements from here*/}
                <div className="row ml-1 mt-5">
                    <ButtonGroup>
                        <Button variant="outline-danger" className="mt-3"
                                onClick={() => setStatus("WAITING_REPLACEMENT")}>
                            DEADLINE PASSED
                        </Button>
                        <Button variant="outline-info" className="mt-3"
                                onClick={() => setStatus("WAITING_BOARD")}>
                            EDITORS ADDED NOTES
                        </Button>
                        <Button variant="outline-warning" className="mt-3"
                                onClick={() => setStatus("WAITING_MAIN_EDITOR")}>
                            BOARD CAN'T DECIDE
                        </Button>
                    </ButtonGroup>
                </div>
                {/*End of test elements*/}
            </div>
        </div>
    );
}