import React, {useEffect, useState} from "react";
import {Button, Form} from "react-bootstrap";
import Select from "react-select";
import {Link} from "react-router-dom";
import {useForm} from "react-hook-form";
import CustomFormField from "../CustomFormField";
import CustomForm from "../CustomForm";

export default function RegisterReader(props) {

    const genres = [
        {value: "fantasy", label: "Fantasy"},
        {value: "romance", label: "Romance"},
        {value: "thriller", label: "Thriller"}
    ];

    const {register, errors, handleSubmit} = useForm();
    const [wantedGenresForm, setWantedGenresForm] = useState(null);

    useEffect(() => {
        const url = window.location.href;
        const IdFromURL = url.split('?')[1].split('=')[1];
        fetch("https://localhost:8080/api/auth/registration/reader-preferences/" + IdFromURL, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                'Accept': 'application/json'
            },
        })
            .then(response => response.json())
            .then(data => {
                setWantedGenresForm(data);
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

    const submitFormHandler = () => {
        window.location.replace('/register-success');

        // let final = prepareDataForSubmit(data);
        // fetch('https://localhost:8080/api/auth/registration/reader-wanted-genres/' + wantedGenresForm.taskId, {
        //     method: "POST",
        //     headers: {
        //         "Content-Type": "application/json",
        //         "Accept": "application/json"
        //     },
        //     body: JSON.stringify(final)
        // })
        //     .then(response => response.json())
        //     .then(data => {
        //         window.location.replace('/register-success');
        //     })
        //     .catch((error) => {
        //         console.log(error)
        //     });
    }

    return (
        <div className="col-4 content bg-dark p-1">
            <div className="m-5 custom-form border-light border pb-5">
                {wantedGenresForm &&
                <CustomForm
                    formFieldsDTO={wantedGenresForm}
                    loggedIn={localStorage.getItem("token")}
                    submittedForm={submitFormHandler}
                    isFileForm={false}
                    checkFlags={false}
                    buttons={
                        [
                            {
                                flagIndex: 0,
                                hasFlag: false,
                                variant: "success",
                                text: "Continue"
                            }
                        ]}/>
                }
            </div>
        </div>
    );
}