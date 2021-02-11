import {Button, Form} from "react-bootstrap";
import Select from "react-select";
import React, {useEffect, useState} from "react";
import Confirmation from "./modals/Confirmation";
import {useForm} from "react-hook-form";

export default function CustomForm({formFieldsDTO, loggedIn, submittedForm, buttons, checkFlags, isFileForm}) {

    const {register, errors, trigger, handleSubmit} = useForm();

    const [flags, setFlags] = useState([]);
    const [first, setFirst] = useState(true);

    useEffect(() => {
        if (first && checkFlags) {
            let tempFlags = [];
            buttons.forEach((button) => {
                if (button.hasFlag) {
                    tempFlags.push(false);
                }
            })
            setFlags(tempFlags);
            setFirst(false);
        }
    }, [flags])

    const handleFlags = (index) => {
        trigger().then(r => {
            if (r) {
                let newFlags = [];
                flags.forEach((flag) => {
                    newFlags.push(flag);
                })
                newFlags[index] = true;
                setFlags(newFlags);
            }
        });
    }

    const onSubmit = (data) => {
        let final = prepareDataForSubmit(data);
        // Setting URL
        let url = formFieldsDTO.postFormEndpoint;
        url += "/" + formFieldsDTO.taskId;
        if (checkFlags) {
            flags.forEach((value) => {
                url += "/" + value;
            })
        }
        // Fetching data
        fetch(url, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + loggedIn,
                "Content-Type": "application/json",
            },
            body: JSON.stringify(final)
        })
            .then(response => response)
            .then(data => {
                submittedForm(data, flags);
            })
            .catch((error) => {
                console.log(error);
                alert(error);
            });
    }

    const prepareDataForSubmit = (data) => {
        let final = [];
        for (let prop in data) {
            final.push({fieldId: prop, fieldValue: data[prop]})
        }
        return final;
    }

    const handleFileUpload = (files) => {
        let url = new URL(formFieldsDTO.postFormEndpoint + formFieldsDTO.taskId);
        let data = [{fieldId: formFieldsDTO.formFields[0].id, fieldValue: files[0].name.split('.')[0]}]
        fetch(url, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + loggedIn,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
            .then(response => response)
            .then(() => {
                submitUploadFile(files);
            })
            .catch((error) => {
                console.log(error);
            });
        //


    }

    const submitUploadFile = (files) => {
        let url = new URL(formFieldsDTO.uploadFileEndpoint + formFieldsDTO.processInstanceId);
        const formData = new FormData();
        formData.append('file', files[0]);
        fetch(url, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + loggedIn
            },
            body: formData
        })
            .then(response => response)
            .then((data) => {
                submittedForm(data);
            })
            .catch((error) => {
                console.log(error);
            });
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

    const addClassToSelectChild = (formFieldId) => {
        document.getElementById("select-id-" + formFieldId).firstElementChild.classList.add("invalid-custom");
    }

    const setHiddenInput = (formField, selectValue) => {
        let input = document.getElementById('hidden-input-' + formField.id);

        let nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, "value").set;
        nativeInputValueSetter.call(
            input,
            Array.isArray(selectValue) ?
                selectValue.filter(sv => sv.value).map(sv => sv.value)
                :
                selectValue.value
        );

        let ev2 = new Event('input', {bubbles: true});
        input.dispatchEvent(ev2);
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
                        multiple={true}
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
                isFileForm &&
                subtypeFile()
            }
            {!isFileForm &&
            <Form className="mt-5 mb-5 w-75" onSubmit={handleSubmit(onSubmit)}>
                {formFieldsDTO.formFields.map((formField) => {
                        return (
                            <Form.Group className="text-left" key={formField.id}>
                                <Form.Label>{formField.label}</Form.Label>
                                {formField.type.name === "string" &&
                                <>
                                    {!formField.properties.subType &&
                                    <>
                                        <Form.Control isInvalid={checkErrors(formField.id)} name={formField.id}
                                                      type={formField.id === "password" ? "password" : "text"}
                                                      placeholder={formField.properties.placeholder}
                                                      ref={register(setRef(formField.validationConstraints))}/>
                                    </>
                                    }
                                    {formField.properties.subType &&
                                    <>
                                        {formField.properties.subType === "select" &&
                                        <>
                                            <Select id={"select-id-" + formField.id}
                                                    className={(checkErrors(formField.id) ? addClassToSelectChild(formField.id) : "") + " text-dark"}
                                                    options={JSON.parse(formField.properties.options)}
                                                    placeholder={formField.properties.placeholder}
                                                    isMulti={formField.properties.multiselect === "true"}
                                                    onChange={(selectValue) => setHiddenInput(formField, selectValue)}
                                            />
                                            <Form.Control id={"hidden-input-" + formField.id}
                                                          isInvalid={checkErrors(formField.id)} name={formField.id}
                                                          type="text" className="hidden"
                                                          ref={register(setRef(formField.validationConstraints))}
                                                          readOnly/>
                                        </>
                                        }
                                        {formField.properties.subType === "textarea" &&
                                        <>
                                            <Form.Control isInvalid={checkErrors(formField.id)} name={formField.id}
                                                          as="textarea" rows={5}
                                                          placeholder={formField.properties.placeholder}
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
                        )
                    }
                )}
                {buttons.map((button, index) => {
                    return (
                        <p>
                            <Button variant={button.variant}
                                    type="submit"
                                    onClick={() => button.hasFlag ? handleFlags(button.flagIndex) : ""}
                                    key={"button-" + index}
                                    className="mt-3 w-100">
                                {button.text}
                            </Button>
                        </p>)
                })
                }
            </Form>
            }
        </>);
}
