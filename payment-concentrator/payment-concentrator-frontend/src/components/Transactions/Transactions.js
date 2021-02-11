import React, {useEffect, useState} from "react";
import {Button, Table} from "react-bootstrap";

export default function Transactions() {

    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        if (JSON.parse(localStorage.getItem("roles")) === "ROLE_ADMIN"){
            fetch("https://localhost:8081/transaction", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + JSON.parse(localStorage.getItem("token"))
                }
            })
                .then(response => response.json())
                .then(data => {
                    setTransactions(data);
                    console.log(data);
                })
                .catch((error) => {
                    console.error("Error", error);
                })
        }
        else {
            fetch("https://localhost:8081/transaction/" + JSON.parse(localStorage.getItem("token")), {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + JSON.parse(localStorage.getItem("token"))
                }
            })
                .then(response => response.json())
                .then(data => {
                    setTransactions(data);
                    console.log(data);
                })
                .catch((error) => {
                    console.error("Error", error);
                })
        }
    }, [])

    return (<div className="Center mt-5 w-100">
        <Table striped bordered hover variant="dark" responsive
               className="w-75">
            <thead>
            <tr>
                <th>#</th>
                <th>Username</th>
                <th>Merchant Order Id</th>
                <th>Merchant Timestamp</th>
                <th>Acquirer Order Id</th>
                <th>Acquirer Timestamp</th>
                <th>Transaction Timestamp</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Payment method</th>
            </tr>
            </thead>
            <tbody>
            {transactions.map((transaction, index) => {
                return <tr key={index}>
                    <th>{index + 1}</th>
                    <th>{transaction.username}</th>
                    <th>{transaction.merchantOrderId}</th>
                    <th>{transaction.merchantTimestamp}</th>
                    <th>{transaction.acqOrderId}</th>
                    <th>{transaction.acqTimestamp}</th>
                    <th>{transaction.timestamp}</th>
                    <th>{transaction.amount}</th>
                    <th>{transaction.status}</th>
                    <th>{transaction.paymentMethod}</th>
                </tr>
            })}
            </tbody>
        </Table>
    </div>);

}