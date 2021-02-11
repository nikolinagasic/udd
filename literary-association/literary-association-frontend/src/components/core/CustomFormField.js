import {Button, Form} from "react-bootstrap";
import Select from "react-select";
import React, {useEffect, useState} from "react";
import Confirmation from "./modals/Confirmation";

export default function CustomFormField({formField, register, errors, handleFileUpload}) {

    const setOptions = (options) => {
        // Rename Objects in SelectDTO instead of Genres (generic) maybe?
        let removeGenre = options.replaceAll("Genre", '');
        removeGenre = removeGenre.replaceAll("CityAndCountry", '');
        removeGenre = removeGenre.replaceAll("value", '"label"');
        removeGenre = removeGenre.replaceAll("id", '"value"');
        removeGenre = removeGenre.replaceAll("=", ":");
        removeGenre = removeGenre.replaceAll("'", '"');
        return JSON.parse(removeGenre);
    }

    const setRef = (constraints) => {
        let refs = "{";
        constraints.forEach((constraint, index) => {
            refs += '"' + constraint.name + '" : ' + constraint.configuration;
            if (index !== (constraints.length - 1)) {
                refs += ","
            }
        })
        refs += "}";
        return JSON.parse(refs);
    }

    const checkErrors = (id) => {
        for (let prop in errors) {
            if (prop === id) {
                return true;
            }
        }

        return false;
    }

    const addClassToSelectChild = () => {
        document.getElementById("select-id").firstElementChild.classList.add("invalid-custom");
    }

    const [showConfirmation, setShowConfirmation] = useState(false);
    const [files, setFiles] = useState([]);
    const [submitted, setSubmitted] = useState(false);
    const hiddenFileInput = React.useRef(null);

    const handleFileChooserChange = (event) => {
        const newFiles = event.target.files;
        setFiles(newFiles);
        handleFileChooserClose();
    };

    const handleFileChooserClick = () => {
        hiddenFileInput.current.click();
    };

    const handleCloseConfirmation = (confirmed) => {
        if (confirmed) {
            setSubmitted(true);
            handleFileUpload(files);
        } else {
            hiddenFileInput.current.value = "";
        }
        setShowConfirmation(false);
    }

    const handleShowConfirmation = () => setShowConfirmation(true);

    const handleFileChooserClose = () => {
        handleShowConfirmation();
    };

    const subtypeFile = () => {
        return (
            <>
                <Confirmation show={showConfirmation} onHide={(confirmed) => handleCloseConfirmation(confirmed)}/>
                <Button onClick={() => handleFileChooserClick()} disabled={submitted} variant="outline-warning"
                        className="mb-4">
                    SUBMIT WORK
                </Button>
                <Form>
                    <Form.File
                        accept="application/pdf"
                        className="hidden"
                        ref={hiddenFileInput}
                        onChange={(event) => handleFileChooserChange(event)}/>
                </Form>
            </>
        )
    }

    return (
        <>
            {
                formField.properties.subType === "file" &&
                subtypeFile()
            }
            {formField.properties.subType !== "file" &&
            <Form.Group controlId={formField.id} className="text-left">
                <Form.Label>{formField.label}</Form.Label>
                {formField.type.name === "string" &&
                <>
                    {!formField.properties.subType &&
                    <>
                        <Form.Control isInvalid={checkErrors(formField.id)} name={formField.id} type="text"
                                      placeholder={formField.properties.placeholder}
                                      ref={register(setRef(formField.validationConstraints))}/>
                    </>
                    }
                    {formField.properties.subType &&
                    <>
                        {formField.properties.subType === "select" &&
                        <>
                            <Select id="select-id"
                                    className={(checkErrors(formField.id) ? addClassToSelectChild() : "") + " text-dark"}
                                    options={setOptions(formField.properties.options)}
                                    placeholder={formField.properties.placeholder}
                                    onChange={(selected) => {
                                        let input = document.getElementById('hidden-input' + formField.id);

                                        let nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, "value").set;
                                        nativeInputValueSetter.call(input, selected.value);

                                        let ev2 = new Event('input', {bubbles: true});
                                        input.dispatchEvent(ev2);
                                    }
                                    }
                            />
                            <Form.Control id={"hidden-input" + formField.id} isInvalid={checkErrors(formField.id)}
                                          name={formField.id} type="text" className="hidden"
                                          ref={register(setRef(formField.validationConstraints))} readOnly/>
                        </>
                        }
                        {formField.properties.subType === "textarea" &&
                        <>
                            <Form.Control isInvalid={checkErrors(formField.id)} name={formField.id} as="textarea"
                                          rows={5} placeholder={formField.properties.placeholder}
                                          ref={register(setRef(formField.validationConstraints))}/>
                        </>
                        }
                    </>
                    }
                    {
                        checkErrors(formField.id) &&
                        <Form.Control.Feedback type="invalid">
                            <span className="text-danger">{formField.id.toUpperCase()} IS INVALID!</span>
                        </Form.Control.Feedback>
                    }
                </>
                }
            </Form.Group>
            }
        </>
    );
}