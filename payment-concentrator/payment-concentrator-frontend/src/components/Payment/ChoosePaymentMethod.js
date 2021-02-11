import BankIcon from "../../images/bank2.png";
import React, {useEffect, useState} from "react";
import PayPalIcon from "../../images/paypal.png";
import BitcoinIcon from "../../images/bitcoin.png";
import Button from "react-bootstrap/Button";

export default function ChoosePaymentMethod(props) {

    const [buyerRequest, setBuyerRequest] = useState(null);

    useEffect(() => {
        if (props?.match?.params?.token) {
            fetch('https://localhost:8081/buyer-request/' + props?.match?.params?.request_id, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + props.match.params.token
                }
            })
                .then(response => response.json())
                .then(data => {
                    if (data.username != null) {
                        setBuyerRequest(data);
                    }
                })
                .catch((error) => {
                    console.error('Error:', error);
                });
        }
    }, [])

    const methodAllowed = (method) => {
        let allowed = false;
        if (buyerRequest) {
            buyerRequest.paymentMethods.forEach((m) => {
                if (m.name === method) {
                    allowed = true;
                    return;
                }
            })
        }
        return allowed;
    }

    const handlePayWithPayPalClick = () => {
        // alert(props.match.params.request_id)
        fetch('https://localhost:8081/pay-pal/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId: 2,
                merchantOrderId: buyerRequest.buyerRequestDTO.merchantOrderId,
                merchantTimestamp: buyerRequest.buyerRequestDTO.merchantTimestamp[0] + '-' + buyerRequest.buyerRequestDTO.merchantTimestamp[1]
                    + '-' + buyerRequest.buyerRequestDTO.merchantTimestamp[2] + ' ' + buyerRequest.buyerRequestDTO.merchantTimestamp[3] + ':' + buyerRequest.buyerRequestDTO.merchantTimestamp[4]
                    + ':' + buyerRequest.buyerRequestDTO.merchantTimestamp[5] + 'Z',
                amount: buyerRequest.buyerRequestDTO.amount,
                buyerRequestId: props.match.params.request_id
            })
        })
            .then(response => response.json())
            .then(data => {
                window.location.replace(data.redirectUrl);
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }

    const handleBitcoinPayment = () => {
        fetch('https://localhost:8081/api/auth/bitcoin/transaction/' + props.match.params.request_id, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.url != null) {
                    console.log(JSON.stringify(data))
                    window.location.replace(data.url);
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }

    const handleBankPayment = () => {
        fetch('https://localhost:8081/api/auth/bank/transaction/' + props.match.params.request_id, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.url != null) {
                    console.log(JSON.stringify(data))
                    window.location.replace(data.url);
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }

    return (
        <div className="bg-light w-75 text-center" style={{marginLeft: "12%", opacity: "0.8", borderRadius: "5px"}}>
            {
                buyerRequest &&
                <>
                    <h2 className="pt-5">Order Details</h2>
                    <div className="mt-5" style={{marginLeft: "25%"}}>
                        <h4 className="text-left">Order Id : {buyerRequest.buyerRequestDTO.merchantOrderId}</h4>
                        <h4 className="text-left">Merchant: {buyerRequest.username}</h4>
                        <h4 className="text-left">Total : {buyerRequest.buyerRequestDTO.amount}$</h4>
                    </div>
                    <h2 className="pt-5">Please choose payment method</h2>
                    <div className="row list-inline-item mt-5 pb-5" style={{placeContent: "center"}}>
                        {methodAllowed("Bank") &&
                        <div className="border border-dark" style={{height: "270px", width: "300px"}}>
                            <img
                                src={BankIcon}
                                style={{height: "200px"}}
                                alt="Bank"
                            />
                            <div className="row" style={{placeContent: "center"}}>
                                <Button variant="danger"
                                        style={{
                                            fontSize: "22px",
                                            paddingLeft: "20px", paddingRight: "20px",
                                            width: "100px"
                                        }}
                                        onClick={() => handleBankPayment()}>
                                    Pay
                                </Button>
                            </div>
                        </div>
                        }
                        {methodAllowed("PayPal") &&
                        <div className="ml-5 border border-dark" style={{height: "270px", width: "300px"}}>
                            <img
                                src={PayPalIcon}
                                style={{height: "200px"}}
                                alt="PayPal"
                            />
                            <div className="row" style={{placeContent: "center"}}>
                                <Button variant="danger"
                                        onClick={() => {
                                            handlePayWithPayPalClick()
                                        }}
                                        style={{
                                            fontSize: "22px",
                                            paddingLeft: "20px",
                                            paddingRight: "20px",
                                            width: "100px"
                                        }}>
                                    Pay
                                </Button>
                            </div>
                        </div>
                        }
                        {methodAllowed("Bitcoin") &&
                        <div className="ml-5 border border-dark" style={{height: "270px", width: "300px"}}>
                            <img
                                src={BitcoinIcon}
                                style={{height: "160px"}}
                                alt="BitCoin"
                                className="mt-3"
                            />
                            <div className="row mt-4" style={{placeContent: "center"}}>
                                <Button variant="danger"
                                        style={{
                                            fontSize: "22px",
                                            paddingLeft: "20px",
                                            paddingRight: "20px",
                                            width: "100px"
                                            }}
                                        onClick = {() => handleBitcoinPayment()}>
                                    Pay
                                </Button>
                            </div>
                        </div>
                        }
                    </div>
                </>
            }
        </div>
    );
}