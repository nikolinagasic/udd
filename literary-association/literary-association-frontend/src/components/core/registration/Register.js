import React, {useEffect, useState} from "react";
import {Button, Form} from "react-bootstrap";
import CustomFormField from "../CustomFormField";
import {useForm} from "react-hook-form";
import CustomForm from "../CustomForm";

export default function Register() {


    const {register, errors, handleSubmit} = useForm();
    const [registerForm, setRegisterForm] = useState(null);

    const [isWriter, setIsWriter] = useState(false);
    const [isBetaReader, setIsBetaReader] = useState(false);

    useEffect(() => {
        fetch("https://localhost:8080/api/auth/registration/user-input-details", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then(response => response.json())
            .then(data => {
                setRegisterForm(data);
                console.error(data);
            })
            .catch((error) => {
                console.log(error);
            });
    }, []);

    const prepareDataForSubmit = (data) => {
        let final = [];
        for (let prop in data) {
            final.push({fieldId: prop, fieldValue: data[prop]})
        }
        return final;
    }

    const submitFormHandler = (data, flags) => {
        if (data.status === 400) {
            alert('Please use different username. This one is taken.');
            return;
        }
        if (flags[1]) {
            window.location.href = '/register-beta-reader?id=' + registerForm.processInstanceId;
        } else {
            window.location.replace('/register-success');
        }
    }


    return (
        <div className="col-4 content bg-dark p-1">
            <div className="m-5 custom-form border-light border pb-5">
                {registerForm &&
                <CustomForm
                    formFieldsDTO={registerForm}
                    loggedIn={localStorage.getItem("token")}
                    submittedForm={submitFormHandler}
                    isFileForm={false}
                    checkFlags={true}
                    buttons={
                        [
                            {
                                flagIndex: 0,
                                hasFlag: true,
                                variant: "success",
                                text: "I wanna be writer"
                            },
                            {
                                hasFlag: false,
                                variant: "warning",
                                text: "I wanna be reader"
                            },
                            {
                                flagIndex: 1,
                                hasFlag: true,
                                variant: "primary",
                                text: "I wanna be beta reader"
                            }
                        ]}/>
                    // <Form className="mt-5 mb-5 w-75 pb-5" onSubmit={handleSubmit(submitFormHandler)}>
                    //     <h3 className="text-left pb-3">Registration</h3>
                    //     {registerForm.formFields.map((formField, index) => {
                    //         return (
                    //             <CustomFormField key={index} formField={formField} errors={errors} register={register}/>
                    //         )
                    //     })
                    //     }
                    //     <Button variant="success" type="submit" className="float-right w-100 mt-3"
                    //             onClick={() => setIsWriter(true)}
                    //     >
                    //         I wanna be writer
                    //     </Button>
                    //     <Button variant="warning" type="submit" className="float-right w-100 mt-3">
                    //         I wanna be reader
                    //     </Button>
                    //     <Button variant="primary" type="submit" className="float-right w-100 mt-3"
                    //             onClick={() => setIsBetaReader(true)}>
                    //         I wanna be beta reader
                    //     </Button>
                    // </Form>
                }
            </div>
        </div>
    );
}