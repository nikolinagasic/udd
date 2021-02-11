import React, {useEffect, useState} from "react";
import {Button, Table} from "react-bootstrap";

export default function Cart(props) {

    const [qFlag, setQFlag] = useState(-1);
    const [cartItemsIds, setCartItemsIds] = useState([]);
    const [amount, setAmount] = useState(0);

    useEffect(() => {
        let calculated = 0;
        let tempQFlag = 0;
        props.cartItems.forEach((item) => {
            tempQFlag += item.quantity;
            for(let i = 0; i < item.quantity; i++){
                calculated += item.book.price;
            }
        })
        setQFlag(tempQFlag);
        setAmount(calculated.toFixed(2));

        if(cartItemsIds.length === qFlag){
            createRequest();
        }
    }, [cartItemsIds])

    const removeBook = (book) => {
        props.setCartItems(props.cartItems.filter(c => c.book.id !== book.id))
    }

    const handleCheckout = () => {
        getItemsIds();
    }

    const getItemsIds = () => {
        let temp = [];
        props.cartItems.forEach((item, index) => {
            for(let i = 0; i < item.quantity; i++){
                temp.push(item.book.id);
            }
        })
        setCartItemsIds(temp);
    }

    const createRequest = () => {
        fetch('https://localhost:8080/api/auth/purchase-book', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body:
                JSON.stringify({
                    amount: amount,
                    bookList: cartItemsIds
                })
        })
            .then(response => response.json())
            .then(data => {
                redirectToPayment(data);
            })
            .catch((error) => {
                console.log('Error:' + error);
            });
    }

    const redirectToPayment = (mainData) => {
        fetch('https://localhost:8081/payment-method/subscriber/' + mainData.token, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + mainData.token
            },
            body:
                JSON.stringify({
                    merchantOrderId: mainData.orderId,
                    merchantTimestamp: mainData.timestamp,
                    amount: mainData.amount
                })
        })
            .then(result => result.json())
            .then(data => {
                props.setCartItems([]);
                setCartItemsIds([]);
                setAmount(0);
                setQFlag(-1);
                window.location.replace(data.url + "/" + mainData.token);
            })
            .catch((error) => {
                console.log('Error:' + error);
            });
    }

    return (
        <div className="bg-dark p-5 cart">
            <div className="bg-dark p-5 border border-light">
                <h2 className="text-left text-light mb-4">
                    Cart
                </h2>
                <Table striped bordered hover variant="dark" className="cart-table">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th></th>
                        <th>Author</th>
                        <th>Title</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    { props.cartItems.map((item, index) => {

                        return (
                            <tr>
                                <td>{index + 1}</td>
                                <td className="text-center"><img style={{maxHeight: "100px", height: "100px"}}
                                                                 src={"../images/books/" + item.book.id + ".jpg"}/></td>
                                <td>{item.book.writer.firstName} {item.book.writer.lastName}</td>
                                <td>{item.book.title}</td>
                                <td>{item.book.price}$</td>
                                <td>{item.quantity}</td>
                                <td className="text-center">
                                    <Button variant="outline-danger" onClick={() => removeBook(item.book)}>
                                        REMOVE
                                    </Button>
                                </td>
                            </tr>
                        )

                      })
                    }
                    </tbody>
                </Table>
                <div className="mb-5 mt-4">
                    <h2 className="float-left text-light">Total price : {amount} $</h2>
                    <Button variant="outline-success" className="float-right checkout" onClick={() => {
                        handleCheckout();
                    }}>
                        CHECKOUT
                    </Button>
                </div>
            </div>
        </div>
    );
}