import React, {useState} from "react";
import {Button, Form} from "react-bootstrap";
import Select from "react-select";
import {Link} from "react-router-dom";

export default function RegisterReader() {

    const [beta, setBeta] = useState(false);

    const genres = [
        { value: "fantasy", label: "Fantasy" },
        { value: "romance", label: "Romance" },
        { value: "thriller", label: "Thriller" }
    ];

    return (
        <div className="col-4 content bg-dark p-1">
            <div className="m-5 custom-form border-light border pb-5">
                <Form className="mt-5 mb-5 w-75 pb-5">
                    <h3 className="text-left pb-3">Please enter more information</h3>
                    <Form.Group controlId="genres_like" className="text-left">
                        <Form.Label>Tell us which genres do you like</Form.Label>
                        <Select className="text-dark" isMulti={true} options={genres}/>
                    </Form.Group>
                    <Form.Group controlId="beta" className="text-left">
                        <Form.Check onChange={(e) => setBeta(e.target.checked)} type="checkbox" label="I wanna be beta reader" title="Beta readers get a chance to read scripts before publishing" />
                    </Form.Group>
                    { beta &&
                        <Link to="/register-beta">
                            <Button variant="primary" type="submit" className="float-right w-100 mt-3">
                                Continue
                            </Button>
                        </Link>

                    }
                    {  !beta &&
                        <Link to="/register-success">
                            <Button variant="success" type="submit" className="float-right w-100 mt-3">
                                Continue
                            </Button>
                        </Link>
                    }
                </Form>
            </div>
        </div>
    );
}