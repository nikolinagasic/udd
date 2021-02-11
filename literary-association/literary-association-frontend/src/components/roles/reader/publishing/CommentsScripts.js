import React, {useEffect, useState} from "react";
import {Button, Table} from "react-bootstrap";
import PreviewPDF from "../../../core/modals/PreviewPDF";
import AddComment from "./AddComment";

export default function CommentsScripts(props) {

    const [showDocument, setShowDocument] = useState(false);
    const [showComment, setShowComment] = useState(false);

    const [requests, setRequests] = useState([]);
    const [selectedRequest, setSelectedRequest] = useState({});

    const [commentForm, setCommentForm] = useState({});

    const handleCloseDocument = () => setShowDocument(false);
    const handleShowDocument = (request) => {
        setSelectedRequest(request);
        setShowDocument(true);
    }

    const handleCloseComment = () => {
        setShowComment(false);
        window.location.reload();
    }
    const handleShowComment = (request) => {
        fetch("https://localhost:8080/publish/beta-reader/" + request.taskId, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + props.loggedIn,
                "Content-Type": "application/json",
            }
        })
            .then(response => response.json())
            .then(data => {
                setCommentForm(data);
                setShowComment(true);
            })
            .catch((error) => {
                console.log(error);
            });
    }

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

    return (
        <div className="bg-dark p-5">
            <PreviewPDF selectedRequest={selectedRequest} show={showDocument} onHide={handleCloseDocument}/>
            <AddComment commentForm={commentForm} show={showComment} onHide={handleCloseComment}/>
            <div className="bg-dark p-5 border border-light">
                <h2 className="text-left text-light mb-4">
                    Review scripts
                </h2>
                <Table striped bordered hover variant="dark">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Username</th>
                        <th>Title</th>
                        <th>Genre</th>
                        <th className="w-25">Synopsys</th>
                        <th>Deadline</th>
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
                                <td className="text-center text-danger" style={{verticalAlign: "middle"}}>
                                    {request.publishBookRequest.deadline}
                                </td>
                                <td className="text-center" style={{verticalAlign: "middle"}}>
                                    <Button variant="outline-warning" onClick={() => handleShowComment(request)}>
                                        ADD COMMENT
                                    </Button>
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