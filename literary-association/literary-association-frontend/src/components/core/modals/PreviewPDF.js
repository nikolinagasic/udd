import React, {useEffect, useState} from 'react';
import {Document, Page} from 'react-pdf';
import {Button, ButtonGroup, Modal} from "react-bootstrap";
import 'react-pdf/dist/esm/Page/AnnotationLayer.css';
import {pdfjs} from 'react-pdf';
import {saveAs} from 'file-saver'

pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

export default function PreviewPDF(props) {
    const [numPages, setNumPages] = useState(null);
    const [pageNumber, setPageNumber] = useState(1);

    useEffect(() => {
        if (props.selectedRequest) {
            console.log(props.selectedRequest)
            fetch("https://localhost:8080/publish/download/" + props.selectedRequest.processInstanceId + ".pdf", {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + props.loggedIn,
                    "Content-Type": "application/octet-stream",
                }
            })
                .then(data => {
                    setFile(data.url);
                    console.log(data)
                })
                .catch((error) => {
                    console.log(error);
                });
        }
    }, [props.selectedRequest])

    function onDocumentLoadSuccess({numPages}) {
        setNumPages(numPages);
    }

    const handleClose = (data, approved) => {
        setPageNumber(1);
        props.onHide(data, approved);
    };

    const [file, setFile] = useState(null);

    const handleDownload = () => {
        if (props.selectedRequest){
            fetch("https://localhost:8080/publish/download/" + props.selectedRequest.processInstanceId + ".pdf", {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + props.loggedIn,
                    "Content-Type": "application/octet-stream",
                    responseType: 'blob',
                }
            })
                .then(response => response.blob())
                .then(data => {
                    saveAs(data, 'script.pdf');
                })
                .catch((error) => {
                    console.log(error);
                })
        }
    }

    const handleDecision = (decision) => {
        fetch("https://localhost:8080/publish/editor/decision/2/" + props.selectedRequest.taskId, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + props.loggedIn,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({approved: decision})
        })
            .then(response => response.json())
            .then(data => {
                handleClose(data, decision);
            })
            .catch((error) => {
                console.log(error);
            });

        if (decision) {
            handleClose(null, decision);
        }
    }

    return (
        <Modal centered show={props.show} onHide={handleClose} className="preview-pdf">
            <Modal.Body className="">
                <Document
                    className="center-document mt-0 pt-0"
                    file={file}
                    onLoadSuccess={onDocumentLoadSuccess}>
                    {   props.selectedRequest?.publishBookRequest?.status === "WAITING_READING" &&
                        <ButtonGroup className="mb-3 mt-2">
                            <Button variant="success"
                                    className={props.selectedRequest.taskIsForm ? "hidden" : ""}
                                    onClick={() => {handleDecision(true)}}>
                                APPROVE
                            </Button>
                            <Button variant="danger" onClick={() => {handleDecision(false)}}>
                                REJECT
                            </Button>
                        </ButtonGroup>
                    }
                    <div className="row controls-center mt-2">
                        <i onClick={() => setPageNumber(pageNumber > 1 ? (pageNumber - 1) : 1)}
                           className="material-icons">keyboard_arrow_left</i>
                        <p>Page {pageNumber} of {numPages}</p>
                        <i onClick={() => setPageNumber(numPages > pageNumber ? (pageNumber + 1) : numPages)}
                           className="material-icons">keyboard_arrow_right</i>
                    </div>
                    <Page className="mb-3" pageNumber={pageNumber}/>
                    <div className="row controls-center">
                        <i onClick={() => setPageNumber(pageNumber > 1 ? (pageNumber - 1) : 1)}
                           className="material-icons">keyboard_arrow_left</i>
                        <p>Page {pageNumber} of {numPages}</p>
                        <i onClick={() => setPageNumber(numPages > pageNumber ? (pageNumber + 1) : numPages)}
                           className="material-icons">keyboard_arrow_right</i>
                    </div>
                    <Button variant="info" onClick={() => handleDownload()}>
                        DOWNLOAD
                    </Button>
                </Document>
            </Modal.Body>
        </Modal>
    );
}
