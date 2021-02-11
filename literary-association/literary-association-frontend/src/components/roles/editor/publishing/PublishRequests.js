import React, {useEffect, useState} from "react";
import {Button, ButtonGroup, Table} from "react-bootstrap";
import AddExplanation from "./AddExplanation";
import PlagiarismCheckResults from "./PlagiarismCheckResults";
import PreviewPDF from "../../../core/modals/PreviewPDF";
import ChooseBetaReaders from "./ChooseBetaReaders";
import AddSuggestions from "./AddSuggestions";
import LoadingOverlay from 'react-loading-overlay';

export default function PublishRequests(props) {

    const [loading, setLoading] = useState(false);

    const [requests, setRequests] = useState([]);

    const [status, setStatus] = useState("");
    const [corrections, setCorrections] = useState(null);

    const [showExplanation, setShowExplanation] = useState(false);
    const [showSuggestions, setShowSuggestions] = useState(false);
    const [showBetaReaders, setShowBetaReaders] = useState(false);
    const [showPlagiarismCheckResults, setShowPlagiarismCheckResults] = useState(false);
    const [showDocument, setShowDocument] = useState(false);

    const [selectedRequest, setSelectedRequest] = useState(null);

    const handleShowExplanation = () => setShowExplanation(true);
    const handleCloseExplanation = () => {
        setShowExplanation(false);
        window.location.reload();
    };

    const handleShowSuggestions = () => setShowSuggestions(true);
    const handleCloseSuggestions = () => {
        setShowSuggestions(false);
        window.location.reload();
    }

    const handleShowPlagiarismCheckResults = (data) => {
        setSelectedRequest(data);
        setShowPlagiarismCheckResults(true);
    }
    const handleClosePlagiarismCheckResults = () => {
        window.location.reload();
        setShowPlagiarismCheckResults(false);
    }

    const handleCloseDocument = (data, approved) => {
        setShowDocument(false);
        if (!approved) {
            setEditorRefuseForm(data);
            handleShowExplanation();
        } else {
            window.location.reload();
        }
    }

    const handleShowDocument = (data) => {
        setSelectedRequest(data);
        setShowDocument(true);
    }

    const [betaReadersForm, setBetaReadersForm] = useState(null);

    const handleCloseBetaReaders = () => {
        setShowBetaReaders(false);
        window.location.reload();
    }
    const handleShowBetaReaders = (data) => {
        fetch("https://localhost:8080/publish/editor/send-to-beta/" + data.taskId, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + props.loggedIn,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                approved: true
            })
        })
            .then(response => response.json())
            .then(data => {
                setBetaReadersForm(data);
                setShowBetaReaders(true);
            })
            .catch((error) => {
                console.log(error);
            });
    }

    const handleSendToLector = (data) => {
        fetch("https://localhost:8080/publish/editor/send-to-beta/" + data.taskId, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + props.loggedIn,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                approved: false
            })
        })
            .then(response => response.json())
            .then(data => {
            })
            .catch((error) => {
                console.log(error);
            });
        window.location.reload();
    }

    useEffect(() => {
        getRequests();
    }, [])

    const getRequests = () => {
        setLoading(true);
        setTimeout(() => {
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
                    setLoading(false);
                })
                .catch((error) => {
                    console.log(error);
                });
        }, 1000);
    }

    const [suggestionForm, setSuggestionForm] = useState({});

    const handleNeedMoreChanges = (needMoreChanges, taskId) => {
        fetch("https://localhost:8080/publish/editor/need-more-changes/" + taskId, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + props.loggedIn,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                approved: needMoreChanges
            })
        })
            .then(response => response.json())
            .then(data => {
                if (needMoreChanges) {
                    setSuggestionForm(data);
                    handleShowSuggestions();
                }
            })
            .catch((error) => {
                console.log(error);
            });

        if (!needMoreChanges) {
            window.location.reload();
        }
    }

    const renderStatus = (data) => {
        switch (data.publishBookRequest.status) {
            case "WAITING_SUBMIT" : {
                return (
                    <>
                        <h5 className="text-warning text-left">
                            Waiting for reader to submit script...
                            Submission deadline : {data.publishBookRequest.deadline}
                        </h5>
                    </>
                );
            }
            case "WAITING_PLAGIARISM_CHECK" : {
                return (
                    <>
                        <Button variant="outline-info" onClick={() => handleShowPlagiarismCheckResults(data)}>
                            PREVIEW PLAGIARISM CHECK RESULTS
                        </Button>
                    </>
                );
            }
            case "WAITING_READING" : {
                return (
                    <>
                        <Button variant="outline-success" onClick={() => handleShowDocument(data)}>
                            READ SCRIPT
                        </Button>
                    </>
                );
            }
            case "WAITING_AFTER_READING" : {
                return (
                    <>
                        <ButtonGroup>
                            <Button variant="outline-info"
                                    onClick={() => handleShowBetaReaders(data)}>
                                SEND TO BETA READERS
                            </Button>
                            <Button variant="outline-warning"
                                    className={data.taskIsForm ? "hidden" : ""}
                                    onClick={() => handleSendToLector(data)}>
                                SEND TO LECTOR
                            </Button>
                        </ButtonGroup>
                    </>
                );
            }
            case "WAITING_BETA_READERS": {
                return (
                    <>
                        <h5 className="text-warning text-left">
                            Waiting for beta readers to comment...
                            Comments deadline : 15.1.2021.
                        </h5>
                    </>
                );
            }
            case "WAITING_COMMENT_CHECK": {
                return (
                    <>
                        <h5 className="text-warning text-left">
                            Writer is reviewing comments...
                        </h5>
                    </>
                );
            }
            case "WAITING_LECTOR_REVIEW": {
                return (
                    <>
                        <h5 className="text-warning text-left">
                            Waiting lector review...
                        </h5>
                    </>
                );
            }
            case "WAITING_CORRECTIONS": {
                return (
                    <>
                        <h5 className="text-warning text-left">
                            Writer is still correcting script...
                        </h5>
                    </>
                );
            }
            case "WAITING_CHANGES": {
                return (
                    <>
                        <h5 className="text-warning text-left">
                            Writer is still changing script...
                            {corrections &&
                            <> Changes deadline : 15.1.2021.</>
                            }
                        </h5>
                    </>
                );
            }
            case "WAITING_SUGGESTIONS": {
                return (
                    <>
                        <ButtonGroup>
                            {data.publishBookRequest.correction &&
                            <Button variant="outline-success"
                                    className={data.taskIsForm ? "hidden" : ""}
                                    onClick={() => handleNeedMoreChanges(null, data.taskId)}>
                                SEND TO PRINTING
                            </Button>
                            }
                            {!data.publishBookRequest.correction &&
                            <Button variant="outline-success"
                                    className={data.taskIsForm ? "hidden" : ""}
                                    onClick={() => handleNeedMoreChanges(false, data.taskId)}>
                                SEND TO LECTOR
                            </Button>
                            }
                            <Button variant="outline-danger"
                                    onClick={() => handleNeedMoreChanges(true, data.taskId)}>
                                GIVE SUGGESTION
                            </Button>
                        </ButtonGroup>
                    </>
                );
            }
            case "DONE": {
                return (
                    <>
                        <h5 className="text-success text-left">
                            ALL DONE
                        </h5>
                    </>
                );
            }
            default: {
                return (
                    <ButtonGroup>
                        <Button variant="outline-success"
                                className={data.taskIsForm ? "hidden" : ""}
                                onClick={() => handleApproveRequest(data, true)}>
                            APPROVE
                        </Button>
                        <Button variant="outline-danger"
                                onClick={() => {
                                    handleApproveRequest(data, false);
                                }}>
                            REJECT
                        </Button>
                    </ButtonGroup>
                );
            }
        }
    }

    const [editorRefuseForm, setEditorRefuseForm] = useState(null);

    const handleApproveRequest = (data, approved) => {
        fetch("https://localhost:8080/publish/editor/decision/" + data.taskId, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + props.loggedIn,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                approved: approved
            })
        })
            .then(response => response.json())
            .then(data => {
                if (!approved) {
                    setEditorRefuseForm(data);
                }
            })
            .catch((error) => {
                console.log(error);
            });
        if (approved) {
            window.location.reload();
        } else {
            handleShowExplanation();
        }
    }

    return (
        <LoadingOverlay
            active={loading}
            spinner
            text="Loading...">
            <div className="bg-dark p-5">
                {editorRefuseForm &&
                <>
                    {editorRefuseForm.formFields &&
                    <AddExplanation show={showExplanation} onHide={handleCloseExplanation} setStatus={setStatus}
                                    editorRefuseForm={editorRefuseForm} loggedIn={props.loggedIn}/>
                    }
                </>
                }
                <AddSuggestions suggestionForm={suggestionForm} show={showSuggestions} onHide={handleCloseSuggestions}
                                setStatus={setStatus}/>
                <ChooseBetaReaders show={showBetaReaders} onHide={handleCloseBetaReaders} setStatus={setStatus}
                                   betaReadersForm={betaReadersForm}/>
                <PlagiarismCheckResults selectedRequest={selectedRequest} show={showPlagiarismCheckResults}
                                        onHide={handleClosePlagiarismCheckResults} setStatus={setStatus}
                                        handleShowExplanation={handleShowExplanation}/>
                <PreviewPDF selectedRequest={selectedRequest} show={showDocument} onHide={handleCloseDocument}
                            status={status} setStatus={setStatus} handleShowExplanation={handleShowExplanation}/>
                <div className="bg-dark p-5 border border-light">
                    <h2 className="text-left text-light mb-4">
                        Publish book requests
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
                                        {renderStatus(request)}
                                    </td>
                                    <td className="text-center" style={{verticalAlign: "middle"}}>
                                        {request.publishBookRequest.status === "WAITING_SUGGESTIONS" &&
                                        <Button variant="outline-info" onClick={() => handleShowDocument(request)}>
                                            READ SCRIPT
                                        </Button>
                                        }
                                    </td>
                                </tr>
                            )
                        })}
                        </tbody>
                    </Table>
                </div>
            </div>
        </LoadingOverlay>
    );
}