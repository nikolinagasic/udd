import React, {useState, useEffect} from "react";
import {useForm} from "react-hook-form";

import "./WelcomeCard.scss";
import {Form, Alert,Button} from "react-bootstrap";
import PaymentMethodsService from "../../services/PaymentMethodsService";

export default function WelcomeCard(props) {
    const [visible, setVisible] = useState(false);
    const [paymentMethods, setPaymentMethods] = useState([]);
    const [choosenPaymentMethods, setChoosenPaymentMethods] = useState([])
    const [showAlert, setShowAlert] = useState(false);
    const [alertVariant, setAlertVariant] = useState('success');

    /* Validation */
    const {register, errors, handleSubmit} = useForm();

    useEffect(() => {
        PaymentMethodsService.getAll().then((data) => setPaymentMethods(data));
    }, []);

    const showFormClickHandler = () => {
        setVisible(true);
    };

    function methodsOnChangeHandler(event) {
        const selectedOptions = [...event.target.selectedOptions].map(o => o.value);
        console.log(selectedOptions);
        const methods = selectedOptions.map(option => {
            return paymentMethods.find(m => m.name === option);
        });
        console.log(methods);
        setChoosenPaymentMethods(methods);
    }

    const createRequestClickHandler = (formData) => {
        formData.paymentMethods = choosenPaymentMethods;
        fetch("https://localhost:8081/api/auth/subscribe", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(formData)
        })
            .then(response => response.json())
            .then((data) => {
                console.log(data);
                setShowAlert(true);
                setAlertVariant('success');
                setTimeout(() => {
                    setShowAlert(false);
                    props.history.push( "/login");
                }, 2000);
            }).catch((error) => {
            setShowAlert(true);
            setAlertVariant('danger');
            setTimeout(() => {
                setShowAlert(false);
            }, 2000);
        });;
    }

    return (
        <div className="Content">
            <div className="WelcomeCard">
                <h1 className="WelcomeTittle">Sign up right now!</h1>
                <p className="WelcomeText">
                    {" "}
                    We are a company that proved payment services to anyone who needs it.
                    We provide different kind of payment services such as via Bank, Paypal
                    and Bitcoin. If you think this is what you need, feel free to submit
                    your request and we will contact you soon!
                </p>
                <button
                    className="Button Green"
                    onClick={showFormClickHandler}>
                    Sign up!
                </button>
            </div>
            <div className="Subscribe p-5 mb-5" hidden={!visible}>
                <Form onSubmit={handleSubmit(createRequestClickHandler)}>
                    <Form.Group
                        className="InputElement">
                        <Alert className="Alert" variant={alertVariant} show={showAlert} dismissible={true} transition={true}>
                            {alertVariant === "success" ? "You have submitted request successfully." : "Please fill in all the data."}
                        </Alert>
                        <Form.Label className="Label text-warning mt-2">ACCOUNT INFO</Form.Label>
                        <Form.Label className="Label">Username</Form.Label>
                        <Form.Control type="text" placeholder="Enter username"
                                      name="username" ref={register({required: true})}
                                      isInvalid={!!errors.username}/>
                        {errors.organizationName &&
                        <Form.Control.Feedback type="invalid">
                            Username is required.
                        </Form.Control.Feedback>}
                    </Form.Group>
                    <Form.Group
                        className="InputElement">
                        <Form.Label className="Label">Password</Form.Label>
                        <Form.Control type="password" placeholder="Enter password"
                                      name="password"
                                      ref={register(
                                            {
                                              required: {
                                                  value: true,
                                                  message: "Password is required."
                                              },
                                              pattern: {
                                                  value: /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-.]).{8,}$/,
                                                  message: "Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character!"
                                              }
                                          })}
                                      isInvalid={!!errors.password}/>
                        {errors.password &&
                        <Form.Control.Feedback type="invalid">
                            {errors.password.message}
                        </Form.Control.Feedback>}
                    </Form.Group>
                    <Form.Group
                        className="InputElement"
                        controlId="exampleForm.ControlInput1">
                        <Form.Label className="Label text-warning mt-2">ORGANIZATION INFO</Form.Label>
                        <Form.Label className="Label">Organization name</Form.Label>
                        <Form.Control type="text" placeholder="Enter organization name"
                                      name="organizationName" ref={register({required: true})}
                                      isInvalid={!!errors.organizationName}/>
                        {errors.organizationName &&
                        <Form.Control.Feedback type="invalid">
                            Organization name is required.
                        </Form.Control.Feedback>}
                    </Form.Group>
                    <Form.Group
                        className="InputElement"
                        controlId="exampleForm.ControlInput2"
                    >
                        <Form.Label className="Label">Organization email</Form.Label>
                        <Form.Control type="text" placeholder="Enter organization email"
                                      name="organizationEmail" ref={register({required: true})}
                                      isInvalid={!!errors.organizationEmail}/>
                        {errors.organizationEmail &&
                        <Form.Control.Feedback type="invalid">
                            Organization email is required.
                        </Form.Control.Feedback>}
                    </Form.Group>
                    <Form.Group
                        controlId="exampleForm.ControlTextarea1"
                        className="InputElement">
                        <Form.Label className="Label">
                            Description of your business
                        </Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={3}
                            placeholder="Describe what you do"
                            style={{resize: "none"}}
                            name="organizationDescription" ref={register({required: true})}
                            isInvalid={!!errors.organizationDescription}
                        />
                        {errors.organizationDescription &&
                        <Form.Control.Feedback type="invalid">
                            Organization description is required.
                        </Form.Control.Feedback>}
                    </Form.Group>
                    <Form.Group
                        controlId="exampleForm.ControlSelect2"
                        className="InputElement">
                        <Form.Label className="Label text-warning mt-2">PAYMENT INFO</Form.Label>
                        <Form.Label className="Label">Select payment methods</Form.Label>
                        { paymentMethods &&
                        <Form.Control as="select" multiple onChange={e => methodsOnChangeHandler(e)}
                            name="organizationPaymentMethods" ref={register({required: true})}
                            isInvalid={!!errors.organizationPaymentMethods}>
                            { paymentMethods.map(method => {
                                return <option key={method.id} value={method.name}>{method.name}</option>
                            })}
                        </Form.Control>
                        }
                        {errors.organizationPaymentMethods &&
                        <Form.Control.Feedback type="invalid">
                            Organization payment methods are required.
                        </Form.Control.Feedback>}
                    </Form.Group>
                    <Form.Group
                        className="InputElement">
                        <Form.Label className="Label">Success URL</Form.Label>
                        <Form.Control type="text" placeholder="Enter success URL"
                                      name="successUrl" ref={register({required: true})}
                                      isInvalid={!!errors.successUrl}/>
                        {errors.successUrl &&
                        <Form.Control.Feedback type="invalid">
                            URL is required.
                        </Form.Control.Feedback>}
                    </Form.Group>
                    <Form.Group
                        className="InputElement">
                        <Form.Label className="Label">Failed URL</Form.Label>
                        <Form.Control type="text" placeholder="Enter failed URL"
                                      name="failedUrl" ref={register({required: true})}
                                      isInvalid={!!errors.failedUrl}/>
                        {errors.failedUrl &&
                        <Form.Control.Feedback type="invalid">
                            URL is required.
                        </Form.Control.Feedback>}
                    </Form.Group>
                    <Form.Group
                        className="InputElement">
                        <Form.Label className="Label">Error URL</Form.Label>
                        <Form.Control type="text" placeholder="Enter error URL"
                                      name="errorUrl" ref={register({required: true})}
                                      isInvalid={!!errors.errorUrl}/>
                        {errors.errorUrl &&
                        <Form.Control.Feedback type="invalid">
                            URL is required.
                        </Form.Control.Feedback>}
                    </Form.Group>
                    {   choosenPaymentMethods.find((method) => method.name === "Bank") &&
                    <>
                        <Form.Group
                            className="InputElement">
                            <Form.Label className="Label text-warning mt-2">BANK PAYMENT INFO</Form.Label>
                            <Form.Label className="Label">Bank merchant ID</Form.Label>
                            <Form.Control type="password" placeholder="Enter bank merchant ID"
                                          name="merchantId" ref={register({required: true})}
                                          isInvalid={!!errors.merchantId}/>
                            {errors.merchantId &&
                            <Form.Control.Feedback type="invalid">
                                Merchant ID is required.
                            </Form.Control.Feedback>}
                        </Form.Group>
                        <Form.Group
                            className="InputElement">
                            <Form.Label className="Label">Bank password</Form.Label>
                            <Form.Control type="password" placeholder="Enter bank merchant password"
                                          name="merchantPassword" ref={register({required: true})}
                                          isInvalid={!!errors.merchantPassword}/>
                            {errors.merchantPassword &&
                            <Form.Control.Feedback type="invalid">
                                Merchant password is required.
                            </Form.Control.Feedback>}
                        </Form.Group>
                    </>
                    }
                    {   choosenPaymentMethods.find((method) => method.name === "PayPal") &&
                    <>
                        <Form.Group
                            className="InputElement">
                            <Form.Label className="Label text-warning mt-2">PAYPAL PAYMENT INFO</Form.Label>
                            <Form.Label className="Label">PayPal client ID</Form.Label>
                            <Form.Control type="password" placeholder="Enter PayPal client ID"
                                          name="clientId" ref={register({required: true})}
                                          isInvalid={!!errors.clientId}/>
                            {errors.clientId &&
                            <Form.Control.Feedback type="invalid">
                                Client ID is required.
                            </Form.Control.Feedback>}
                        </Form.Group>
                        <Form.Group
                            className="InputElement">
                            <Form.Label className="Label">PayPal client password</Form.Label>
                            <Form.Control type="password" placeholder="Enter PayPal client password"
                                          name="clientPassword" ref={register({required: true})}
                                          isInvalid={!!errors.clientPassword}/>
                            {errors.clientPassword &&
                            <Form.Control.Feedback type="invalid">
                                Client password is required.
                            </Form.Control.Feedback>}
                        </Form.Group>
                    </>
                    }
                    {   choosenPaymentMethods.find((method) => method.name === "Bitcoin") &&
                        <Form.Group
                            className="InputElement">
                            <Form.Label className="Label text-warning mt-2">BITCOIN PAYMENT INFO</Form.Label>
                            <Form.Label className="Label">Bitcoin token</Form.Label>
                            <Form.Control type="password" placeholder="Enter Bitcoin token"
                                          name="bitcoinToken" ref={register({required: true})}
                                          isInvalid={!!errors.bitcoinToken}/>
                            {errors.bitcoinToken &&
                            <Form.Control.Feedback type="invalid">
                                Bitcoin token is required.
                            </Form.Control.Feedback>}
                        </Form.Group>
                    }
                    <div className="ButtonWrapper">
                        <button className="Button Gray mt-3" type="submit"
                        >Subscribe
                        </button>
                    </div>
                </Form>
            </div>
        </div>
    );
}
