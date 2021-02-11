import React, {useEffect, useState} from "react";
import {Table, Button, ButtonGroup} from "react-bootstrap";
import DocumentsComments from "./DocumentsComments";
import PreviewPDF from "../../../core/modals/PreviewPDF";

export default function RegistrationRequests() {

    const [registrationRequests, setRegistrationRequests] = useState(null);
    const [selectedRequest, setSelectedRequest] = useState(null);
    const [index, setIndex] = useState(null);

    useEffect(() => {
        fetch("https://localhost:8080/api/registration/writer-submitted-work", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + JSON.parse(localStorage.getItem("token")),
                "Content-Type": "application/json",
            },
        })
            .then(response => response.json())
            .then(data => {
                setRegistrationRequests(data);
            })
            .catch((error) => {
                console.log(error)
            });
    }, []);

    const [showDocumentsComments, setShowDocumentsComments] = useState(false);
    const [showDocuments, setShowDocuments] = useState(false);

    const handleCloseDocumentsComments = () => setShowDocumentsComments(false);
    const handleShowDocumentsComments = () => setShowDocumentsComments(true);

    const handleHideModal = () => {
        setShowDocuments(false);
    }

    const resetRequests = () => {
        fetch("https://localhost:8080/api/registration/writer-submitted-work", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + JSON.parse(localStorage.getItem("token")),
                "Content-Type": "application/json",
            },
        })
            .then(response => response.json())
            .then(data => {
                setRegistrationRequests(data);
            })
            .catch((error) => {
                console.log(error)
            });
    }

    return (
        <div className="bg-dark p-5">
            <PreviewPDF selectedRequest={selectedRequest} show={showDocuments} suffix={index + 1}
                        onHide={handleHideModal}/>
            {selectedRequest &&
            <DocumentsComments show={showDocumentsComments} selectedRequest={selectedRequest}
                               hasCallback={true} callBack={resetRequests}
                               onHide={handleCloseDocumentsComments}/>
            }
            <div className="bg-dark p-5 border border-light">
                <h2 className="text-left text-light mb-4">
                    Registration requests
                </h2>
                <Table striped bordered hover variant="dark">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        registrationRequests &&
                        registrationRequests.map((request, index) => {
                            return (
                                <tr>
                                    <td>{index + 1}</td>
                                    <td>{request.writerFirstName}</td>
                                    <td>{request.writerLastName}</td>
                                    <td>
                                        <Button variant="outline-light" onClick={() => {
                                            handleShowDocumentsComments();
                                            setSelectedRequest(request);
                                        }}>
                                            Leave feedback
                                        </Button>
                                    </td>
                                    <td>
                                        {
                                            request.filenames &&
                                            request.filenames.map((filename, index) => {
                                                return (
                                                    <Button
                                                        className="mr-2"
                                                        onClick={() => {
                                                            setSelectedRequest(request);
                                                            setIndex(index);
                                                            setShowDocuments(true);
                                                        }}>
                                                        Read work {index + 1}
                                                    </Button>
                                                );
                                            })
                                        }
                                    </td>
                                </tr>
                            );
                        })
                    }
                    </tbody>
                </Table>
            </div>
        </div>
    );
}