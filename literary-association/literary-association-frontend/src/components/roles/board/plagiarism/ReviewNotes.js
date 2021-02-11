import React, {useState} from "react";
import {Button, Table} from "react-bootstrap";
import AllNotes from "./AllNotes";

export default function ReviewNotes() {

    const [showAllNotes, setShowAllNotes] = useState(false);

    const handleCloseAllNotes = () => setShowAllNotes(false);
    const handleShowAllNotes = () => setShowAllNotes(true);

    return (
        <div className="bg-dark p-5">
            <AllNotes show={showAllNotes} onHide={handleCloseAllNotes}/>
            <div className="bg-dark p-5 border border-light text-left">
                <h2 className="text-left text-light mb-4">
                    Review plagiarism notes
                </h2>
                <Table striped bordered hover variant="dark">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Original</th>
                        <th>Potential plagiat</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>1</td>
                        <td>
                            Something is behind you, Neil Gaiman
                        </td>
                        <td>
                            Get out, Mike Collins
                        </td>
                        <td className="text-center">
                            <Button variant="outline-warning" onClick={() => handleShowAllNotes()}>
                                SHOW NOTES
                            </Button>
                        </td>
                    </tr>
                    </tbody>
                </Table>
            </div>
        </div>
    );
}