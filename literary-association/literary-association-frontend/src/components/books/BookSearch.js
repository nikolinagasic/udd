import React from "react";
import {Button, Form} from "react-bootstrap";
import Select from "react-select";

export default function BookSearch() {

    const genres = [
        { value: "fantasy", label: "Fantasy" },
        { value: "romance", label: "Romance" },
        { value: "thriller", label: "Thriller" }
    ];

    return (
        <div className="book-search container-fluid bg-dark h-100 text-light text-left p-4">
            <div className="custom-form border-light border h-100 p-5">
                <Form className="mb-5 w-100">
                    <Form.Group controlId="title" className="text-left">
                        <Form.Label>Book title</Form.Label>
                        <Form.Control type="text" placeholder="Book title" />
                    </Form.Group>
                    <Form.Group controlId="autor" className="text-left">
                        <Form.Label>Autor</Form.Label>
                        <Form.Control type="text" placeholder="Name and surname" />
                    </Form.Group>
                    <Form.Group controlId="genres" className="text-left">
                        <Form.Label>Genre</Form.Label>
                        <Select className="text-dark" isMulti={true} options={genres}/>
                    </Form.Group>
                    <Form.Group controlId="content" className="text-left">
                        <Form.Label>Content</Form.Label>
                        <Form.Control as="textarea" rows={5} placeholder="Book content" />
                    </Form.Group>
                    <Button variant="danger" type="submit" className="float-right w-100 mt-3">
                        Search
                    </Button>
                </Form>
            </div>
        </div>
    );
}