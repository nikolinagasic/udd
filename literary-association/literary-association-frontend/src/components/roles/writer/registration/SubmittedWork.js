import React, {useEffect, useState} from "react";
import {Button, Table, Form} from "react-bootstrap";
import PreviewComments from "./PreviewComments";
import PreviewPDF from "../../../core/modals/PreviewPDF";
import Confirmation from "../../../core/modals/Confirmation";
import CustomForm from "../../../core/CustomForm";

export default function SubmittedWork() {

    // const [submitted, setSubmitted] = useState(true);
    //
    // const [allReviewed, setAllReviewed] = useState(false);
    //
    // const [attemptsNumber, setAttemptsNumber] = useState(2);

    const [membershipRequest, setMembershipRequest] = useState(null);
    const [publishWorkForm, setPublishWorkForm] = useState(null);

    useEffect(() => {
        const token = JSON.parse(localStorage.getItem("token"));
        fetch('https://localhost:8080/api/registration/request/self/' + token, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json",
            },
        })
            .then(response => response.json())
            .then(data => {
                setMembershipRequest(data);

                fetch("https://localhost:8080/api/registration/upload-work-form", {
                    method: "GET",
                    headers: {
                        "Authorization": "Bearer " + token,
                        "Content-Type": "application/json",
                    },
                })
                    .then(response => response.json())
                    .then(data => {
                        setPublishWorkForm(data);
                    })
                    .catch((error) => {
                        console.log(error);
                    });

            })
            .catch((error) => {
                console.log(error);
            });
    }, []);


    const handleFileUpload = () => {
        const token = JSON.parse(localStorage.getItem("token"));
        fetch('https://localhost:8080/api/registration/request/self/' + token, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json",
            },
        })
            .then(response => response.json())
            .then(data => {
                setMembershipRequest(data);
                setPublishWorkForm(null);
                fetch("https://localhost:8080/api/registration/upload-work-form", {
                    method: "GET",
                    headers: {
                        "Authorization": "Bearer " + token,
                        "Content-Type": "application/json",
                    },
                })
                    .then(response => response.json())
                    .then(data => {
                        setPublishWorkForm(data);
                    })
                    .catch((error) => {
                        console.log(error);
                    });

            })
            .catch((error) => {
                console.log(error);
            });
    }

    return (
        <div className="bg-dark p-5">
            {/*<PreviewComments show={showComments} onHide={handleCloseComments}/>*/}
            {/*<PreviewPDF show={showDocument} onHide={handleCloseDocument}/>*/}
            {/*<Confirmation show={showConfirmation} onHide={(confirmed) => handleCloseConfirmation(confirmed)}/>*/}
            <div className="bg-dark p-5 border border-light text-left">
                {membershipRequest &&
                <>
                    <h2 className="text-light mb-4">
                        Submitted Work
                    </h2>
                    <h5 className="text-danger">
                        Submission deadline : {new Date(membershipRequest.submissionDeadline * 1000).toString()}
                    </h5>
                    <h5 className="text-warning mb-4">
                        Attempts number : {membershipRequest.attemptsNumber}
                    </h5>
                    <h5 className="text-warning mb-4">
                        Files posted : {membershipRequest.filesPosted}
                    </h5>
                </>
                }
                {publishWorkForm &&
                <div hidden={membershipRequest.filesPosted >= 2 && membershipRequest.status === "WAITING_OPINION"}>
                    <CustomForm
                        formFieldsDTO={publishWorkForm}
                        loggedIn={localStorage.getItem("token")}
                        submittedForm={handleFileUpload}
                        isFileForm={true}
                        checkFlags={false}
                        buttons={
                            [
                                {
                                    flagIndex: 0,
                                    hasFlag: false,
                                    variant: "success",
                                    text: "Submit work"
                                }
                            ]}/>
                </div>
                }
            </div>
        </div>
    );
}