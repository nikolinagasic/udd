import React, {useState} from "react";
import {Button, Card, Modal} from "react-bootstrap";
import {Link} from "react-router-dom";

export default function BookDetails(props) {

    const [quantity, setQuantity] = useState(1);

    const handleClose = () => {
        setQuantity(1);
        props.onHide();
    };

    const addToCart = () => {
        if (props.cartItems.length > 0){
            props.setCartItems([...props.cartItems,  {book:props.book, quantity:quantity}]);
        }
        else {
            props.setCartItems([{book:props.book, quantity:quantity}]);
        }
        handleClose();
    }

    return (
        <Modal centered show={props.show} onHide={handleClose} className="w-100">
            <Modal.Body>
                <Card className="row border-0">
                    <Card.Header className="col-6 border-0">
                        <Card.Img variant="top" src={"../images/books/" + props.book?.id + ".jpg"} />
                    </Card.Header>
                    <div className="col-6">
                        <Card.Body className="border-0">
                            <Card.Title>{props.book?.title}</Card.Title>
                            <Card.Subtitle className="mb-2 text-muted">{props.book?.genre.value}</Card.Subtitle>
                            <Card.Subtitle className="mb-2 text-muted">ISBN : {props.book?.isbn}</Card.Subtitle>
                            <Card.Text className="mb-0 font-weight-bold">Writer : {props.book?.writer.firstName} {props.book?.writer.lastName}</Card.Text>
                            <Card.Text className="font-weight-bold">Publisher : {props.book?.publisher.name}</Card.Text>
                            <Card.Text className="book-synopsys text-justify">
                                {props.book?.synopsis}
                            </Card.Text>
                            <Card.Text className="font-weight-bold">
                                <Link to="/pricing">
                                    <div className="row ml-1">
                                        <i className="material-icons">get_app</i>
                                        DOWNLOAD BOOK
                                    </div>
                                </Link>
                            </Card.Text>
                        </Card.Body>
                        <Card.Footer className="ml-3">
                            <div className="row row-cols-1 mt-3">
                                <h3 className="text-danger mb-5">{props.book?.price} $</h3>
                            </div>
                            <div className="row">
                                <Button disabled={quantity === 1} variant="outline-dark" className="col-1 add-remove-button"
                                        onClick={() => {
                                            setQuantity(quantity - 1);
                                        }}>
                                    <i className="material-icons">remove</i>
                                </Button>
                                <input className="w-25 col-2" defaultValue="1" value={quantity}/>
                                <Button variant="outline-dark" className="col-1 mr-2 add-remove-button" onClick={() => {
                                    setQuantity(quantity + 1);
                                }}>
                                    <i className="material-icons">add</i>
                                </Button>
                                <Button variant="success" className="col-6 ml-5 float-right cart-button" onClick={() => addToCart()}>
                                    ADD TO CART
                                    <i className="material-icons ml-3">shopping_cart</i>
                                </Button>
                            </div>
                        </Card.Footer>
                    </div>
                </Card>
            </Modal.Body>
        </Modal>
    );
}