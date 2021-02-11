import React, {useState} from "react";
import {Button, Table} from "react-bootstrap";
import PreviewPDF from "../../../core/modals/PreviewPDF";
import AddNote from "./AddNote";

export default function NotesComplaints() {

    const [showDocument, setShowDocument] = useState(false);
    const [showAddNote, setShowAddNote] = useState(false);

    const handleCloseDocument = () => setShowDocument(false);
    const handleShowDocument = () => setShowDocument(true);

    const handleCloseAddNote = () => setShowAddNote(false);
    const handleShowAddNote = () => setShowAddNote(true);

    return (
        <div className="bg-dark p-5">
            <AddNote show={showAddNote} onHide={handleCloseAddNote}/>
            <PreviewPDF show={showDocument} onHide={handleCloseDocument}/>
            <div className="bg-dark p-5 border border-light text-left">
                <h2 className="text-left text-light mb-4">
                    Plagiarism comparation requests
                </h2>
                <Table striped bordered hover variant="dark">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Original</th>
                        <th>Potential plagiat</th>
                        <th>Deadline</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>1</td>
                        <td>
                            Something is behind you, Neil Gaiman
                            <Button variant="outline-info" className="ml-5" onClick={() => handleShowDocument()}>
                                PREVIEW WORK
                            </Button>
                        </td>
                        <td>
                            Get out, Mike Collins
                            <Button variant="outline-info" className="ml-5" onClick={() => handleShowDocument()}>
                                PREVIEW WORK
                            </Button>
                        </td>
                        <td className="text-danger font-weight-bold" style={{verticalAlign:"middle"}}>
                            12.1.2021.
                        </td>
                        <td className="text-center">
                            <Button variant="outline-warning" onClick={() => handleShowAddNote()}>
                                ADD NOTE
                            </Button>
                        </td>
                    </tr>
                    </tbody>
                </Table>
            </div>
        </div>
    );
}