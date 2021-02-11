import React, {useEffect, useState} from "react";
import {Carousel} from "react-bootstrap";
import BankIcon from "../../images/bank2.png";
import PayPalIcon from "../../images/paypal.png";
import BitcoinIcon from "../../images/bitcoin.png";
import NewPaymentIcon from "../../images/payments.jpg";
import "./PaymentMethods.css";
import PaymentMethodsService from "../../services/PaymentMethodsService";

export default function PaymentMethodsComponent() {
    const [otherMethods, setOtherMethods] = useState([]);

    useEffect(() => {
        PaymentMethodsService.getAllWithoutFirstThree().then((data) => setOtherMethods(data));
    }, []);

    return (
        <Carousel>
            <Carousel.Item interval={5000}>
                <img
                    src={BankIcon}
                    width="400px"
                    height="400px"
                    alt="First slide"
                    style={{
                        marginTop: "5%",
                        marginLeft: "37%",
                        backgroundColor: "#f8f9fa",
                    }}
                />

                <Carousel.Caption style={{display: "contents"}}>
                    <h3>Bank</h3>
                    <p>
                        We offer service of paying for items via bank. All you need to have
                        is an account in a bank.
                    </p>
                </Carousel.Caption>
            </Carousel.Item>
            <Carousel.Item interval={5000}>
                <img
                    src={PayPalIcon}
                    width="500px"
                    height="400px"
                    alt="First slide"
                    style={{
                        marginTop: "5%",
                        marginLeft: "35%",
                        backgroundColor: "#f8f9fa",
                    }}
                />

                <Carousel.Caption style={{display: "contents"}}>
                    <h3>PayPal</h3>
                    <p>
                        We offer service of paying for items via paypal. All you need to
                        have is a paypal account.
                    </p>
                </Carousel.Caption>
            </Carousel.Item>
            <Carousel.Item interval={5000}>
                <img
                    src={BitcoinIcon}
                    width="500px"
                    height="400px"
                    alt="First slide"
                    style={{
                        marginTop: "5%",
                        marginLeft: "33%",
                        backgroundColor: "#f8f9fa",
                    }}
                />

                <Carousel.Caption style={{display: "contents"}}>
                    <h3>Bitcoin</h3>
                    <p>
                        We offer service of paying for items via bitcoin. All you need to
                        have is a bitcoin wallet.
                    </p>
                </Carousel.Caption>
            </Carousel.Item>
            {otherMethods &&
            <>
                {otherMethods.map(method => {
                    return (<Carousel.Item interval={5000} key={method.id}>
                        <img
                            src={NewPaymentIcon}
                            width="500px"
                            height="400px"
                            alt="First slide"
                            style={{
                                marginTop: "5%",
                                marginLeft: "33%",
                                backgroundColor: "#f8f9fa",
                            }}
                        />

                        <Carousel.Caption style={{display: "contents"}}>
                            <h3>{method.name}</h3>
                            <p>
                                We offer service of paying for items via {method.name}.
                            </p>
                        </Carousel.Caption>
                    </Carousel.Item>);
                })}
            </>
            }
        </Carousel>
    );
}
