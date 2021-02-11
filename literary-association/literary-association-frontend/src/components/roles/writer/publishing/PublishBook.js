import React, {useEffect, useState} from "react";
import {Table} from "react-bootstrap";
import CustomForm from "../../../core/CustomForm";

export default function PublishBook(props) {

    const [publishBookForm, setPublishBookForm] = useState(null);

    const [requestInfo, setRequestInfo] = useState(null);

    const [status, setStatus] = useState("");

    useEffect(() => {
        fetch("https://localhost:8080/publish/writer/form/" + props.loggedIn, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + props.loggedIn,
                "Content-Type": "application/json",
            },
        })
            .then(response => response.json())
            .then(data => {
                if (data.formFields) {
                    setPublishBookForm(data);
                } else {
                    handleRequestInfo();
                }
            })
            .catch((error) => {
                console.log(error);
            });
    }, [])

    const handleRequestInfo = () => {
        fetch("https://localhost:8080/publish/writer/status/" + props.loggedIn, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + props.loggedIn,
                "Content-Type": "application/json",
            },
        })
            .then(response => response.json())
            .then(data => {
                setRequestInfo(data);
                if (data.status) {
                    setStatus(data.status);
                    if (data.status === "WAITING_SUBMIT" ||
                        data.status === "WAITING_COMMENT_CHECK" ||
                        data.status === "WAITING_CHANGES" ||
                        data.status === "WAITING_CORRECTION") {
                        getFileFormField();
                    }
                } else {
                    setStatus("NO STATUS");
                }
            })
            .catch((error) => {
                console.log(error);
            });
    }

    const getFileFormField = () => {
        fetch("https://localhost:8080/publish/writer/form/upload/" + props.loggedIn, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + props.loggedIn,
                "Content-Type": "application/json",
            },
        })
            .then(response => response.json())
            .then(data => {
                setPublishBookForm(data);
            })
            .catch((error) => {
                console.log(error);
            });
    }

    const submittedForm = () => {
        setPublishBookForm(null);
    }

    const renderStatus = (status) => {
        switch (status) {
            case "WAITING_PLAGIARISM_CHECK" :
                return (
                    <h5 className="text-danger mb-3">
                        Waiting for plagiarism check ...
                    </h5>
                )
            case "WAITING_READING" :
                return (
                    <>
                        <h5 className="text-success mb-3">
                            Your script has passed plagiarism check!
                        </h5>
                        <h5 className="text-danger mb-3">
                            Waiting for editor reading ...
                        </h5>
                    </>
                )
            case "WAITING_BETA_READERS" :
                return (
                    <>
                        <h5 className="text-success mb-3">
                            Your script has been read!
                        </h5>
                        <h5 className="text-danger mb-3">
                            Waiting for beta readers to comment ...
                        </h5>
                    </>
                )
            case "WAITING_LECTOR_REVIEW" :
                return (
                    <>
                        <h5 className="text-success mb-3">
                            Almost there...
                        </h5>
                        <h5 className="text-danger mb-3">
                            Waiting for lector review ...
                        </h5>
                    </>
                )
            case "WAITING_SUGGESTIONS" :
                return (
                    <>
                        <h5 className="text-danger mb-3">
                            Waiting for editor suggestions ...
                        </h5>
                    </>
                )
            case "WAITING_CORRECTION" :
                return (
                    <>
                        <h5 className="text-danger mb-3">
                            Lector correction : {requestInfo.correction}
                        </h5>
                    </>
                )
            case "WAITING_CHANGES" :
                return (
                    <>
                        <h5 className="text-danger mb-3">
                            Editor suggestion : {requestInfo.suggestion}
                        </h5>
                    </>
                )
            case "WAITING_COMMENT_CHECK" :
                return (
                    <>
                        <Table striped bordered hover variant="dark">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Beta Reader</th>
                                <th>Comment</th>
                            </tr>
                            </thead>
                            <tbody>
                            {requestInfo.betaReaderCommentList?.map((comment, index) => {
                                return (
                                    <tr>
                                        <td>{index + 1}</td>
                                        <td>{comment.reader}</td>
                                        <td>
                                            {comment.text}
                                        </td>
                                    </tr>
                                )
                            })
                            }
                            </tbody>
                        </Table>
                    </>
                )
            case "DONE" :
                return (
                    <>
                        <h5 className="text-success mb-3">
                            All good, your book is sent to printing!
                        </h5>
                    </>
                )
        }
    }

    return (
        <div className="bg-dark p-5">
            <div className="bg-dark p-5 border border-light text-left text-light">
                <h2 className="text-light mb-4">
                    Publish book
                </h2>
                {!status && !publishBookForm &&
                <>
                    <h5 className="text-warning mt-3 mb-3">
                        Please reload page for more info ...
                    </h5>
                </>
                }
                {!status && publishBookForm &&
                <CustomForm formFieldsDTO={publishBookForm}
                            loggedIn={props.loggedIn}
                            submittedForm={submittedForm}
                            isFileForm={false}
                            checkFlags={false}
                            buttons={
                                [
                                    {
                                        variant: "outline-success",
                                        text: "SEND REQUEST"
                                    }
                                ]
                            }
                    // buttons={
                    //     [
                    //         {
                    //             flagIndex: 0,
                    //             hasFlag: true,
                    //             variant: "outline-success",
                    //             text:"SEND REQUEST 1"
                    //         },
                    //         {
                    //             hasFlag: false,
                    //             variant: "outline-success",
                    //             text:"SEND REQUEST 2"
                    //         },
                    //         {
                    //             flagIndex: 1,
                    //             hasFlag: true,
                    //             variant: "outline-success",
                    //             text:"SEND REQUEST 3"
                    //         }
                    //     ]
                />
                }
                {status &&
                <>
                    <h5 className="text-light mb-3">
                        Title : {requestInfo?.title}
                    </h5>
                    <h5 className="text-light mb-3">
                        Genre : {requestInfo?.genre}
                    </h5>
                    <h5 className="text-light mb-3 w-25 text-justify">
                        Synopsis : {requestInfo?.synopsis}
                    </h5>
                    {status === "NO STATUS" &&
                    <h5 className="text-warning mt-3 mb-3">
                        Main editor is reviewing your request ...
                    </h5>
                    }
                </>
                }
                {(status === "WAITING_SUBMIT" || status === "WAITING_COMMENT_CHECK" || status === "WAITING_CORRECTION" || status === "WAITING_CHANGES") &&
                <>
                    <h5 className="text-danger mb-3">
                        Submission deadline : {requestInfo.deadline}
                    </h5>
                    {publishBookForm &&
                    <CustomForm formFieldsDTO={publishBookForm}
                                loggedIn={props.loggedIn}
                                submittedForm={submittedForm}
                                isFileForm={true}/>
                    }
                </>
                }
                {renderStatus(status)}
            </div>
        </div>
    );
}