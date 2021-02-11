import React from "react";
import {Button, ButtonGroup, Modal, Table} from "react-bootstrap";

export default function AllNotes(props) {

    const handleClose = () => {
        props.onHide();
    };

    return (
        <Modal centered show={props.show} onHide={handleClose} className="preview-comments">
            <Modal.Body className="bg-light">
                <div className="bg-light p-5">
                    <ButtonGroup>
                        <Button variant="danger">
                            PLAGIAT
                        </Button>
                        <Button variant="success">
                            NOT PLAGIAT
                        </Button>
                    </ButtonGroup>
                    <Table striped bordered hover variant="light">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Note</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>1</td>
                            <td>
                                It's gonna take a lot to take me away from you There's nothing that a hundred men or more could ever do I bless the rains down in Africa Gonna take some time to do the things we never have.
                            </td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td>
                                You can find me in the club, bottle full of bub Look mami I got the X if you into taking drugs I'm into having sex, I ain't into makin love So come give me a hug if you into getting rubbed.
                            </td>
                        </tr>
                        <tr>
                            <td>3</td>
                            <td>
                                Don't want to close my eyes I don't want to fall asleep Cause I'd miss you babe And I don't want to miss a thing Cause even when I dream of you The sweetest dream will never do I'd still miss you babe And I don't want to miss a thing.
                            </td>
                        </tr>
                        <tr>
                            <td>4</td>
                            <td>
                                I see trees of green........ red roses too I see em bloom..... for me and for you And I think to myself.... what a wonderful world.
                            </td>
                        </tr>
                        <tr>
                            <td>5</td>
                            <td>
                                Buddy you're a young man hard man Shoutin' in the street gonna take on the world some day You got blood on yo' face You big disgrace Wavin' your banner all over the place.</td>
                        </tr>
                        </tbody>
                    </Table>
                </div>
            </Modal.Body>
        </Modal>
    )
}