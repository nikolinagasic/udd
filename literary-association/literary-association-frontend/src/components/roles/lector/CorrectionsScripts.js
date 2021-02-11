import React, {useEffect, useState} from "react";
import {Button, ButtonGroup, Table} from "react-bootstrap";
import PreviewPDF from "../../core/modals/PreviewPDF";
import AddCorrections from "./AddCorrections";

export default function CorrectionsScripts(props) {

    const [showDocument, setShowDocument] = useState(false);
    const [showCorrections, setShowCorrections] = useState(false);

    const [requests, setRequests] = useState([]);
    const [selectedRequest, setSelectedRequest] = useState({});

    const handleCloseDocument = () => setShowDocument(false);
    const handleShowDocument = (request) => {
        setSelectedRequest(request);
        setShowDocument(true);
    }

    const handleCloseCorrections = () => {
        setShowCorrections(false);
        window.location.reload();
    }
    const handleShowCorrections = () => setShowCorrections(true);

    useEffect(() => {
        getRequests();
    }, [])

    const getRequests = () => {
        fetch("https://localhost:8080/publish/requests/" + props.loggedIn, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + props.loggedIn,
                "Content-Type": "application/json",
            },
        })
            .then(response => response.json())
            .then(data => {
                setRequests(data);
            })
            .catch((error) => {
                console.log(error);
            });
    }

    const [correctionForm, setCorrectionForm] = useState({});

    const handleNeedCorrection = (needCorrection, taskId) => {
        fetch("https://localhost:8080/publish/lector/need-correction/" + taskId, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + props.loggedIn,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                approved: needCorrection
            })
        })
            .then(response => response.json())
            .then(data => {
                if (needCorrection) {
                    setCorrectionForm(data);
                    handleShowCorrections();
                }
            })
            .catch((error) => {
                console.log(error);
            });

        if (!needCorrection) {
            window.location.reload();
        }
    }

    return (
        <div className="bg-dark p-5">
            <PreviewPDF selectedRequest={selectedRequest} show={showDocument} onHide={handleCloseDocument}/>
            <AddCorrections correctionForm={correctionForm} show={showCorrections} onHide={handleCloseCorrections}/>
            <div className="bg-dark p-5 border border-light">
                <h2 className="text-left text-light mb-4">
                    Correction requests
                </h2>
                <Table striped bordered hover variant="dark">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Username</th>
                        <th>Title</th>
                        <th>Genre</th>
                        <th className="w-25">Synopsys</th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {requests.map((request, index) => {
                        return (
                            <tr key={index}>
                                <td>{index + 1}</td>
                                <td>{request.publishBookRequest.writer}</td>
                                <td>{request.publishBookRequest.title}</td>
                                <td>{request.publishBookRequest.genre}</td>
                                <td>
                                    {request.publishBookRequest.synopsis}
                                </td>
                                <td className="text-center" style={{verticalAlign: "middle"}}>
                                    <ButtonGroup>
                                        <Button variant="outline-warning"
                                                onClick={() => handleNeedCorrection(true, request.taskId)}>
                                            ADD CORRECTIONS
                                        </Button>
                                        <Button variant="outline-success"
                                                className={request.taskIsForm ? "hidden" : ""}
                                                onClick={() => handleNeedCorrection(false, request.taskId)}>
                                            SEND TO EDITOR
                                        </Button>
                                    </ButtonGroup>
                                </td>
                                <td className="text-center" style={{verticalAlign: "middle"}}>
                                    <Button variant="outline-info" onClick={() => handleShowDocument(request)}>
                                        READ SCRIPT
                                    </Button>
                                </td>
                            </tr>
                        )
                    })}
                    </tbody>
                </Table>
            </div>
        </div>
    );
}