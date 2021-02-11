import React, {useEffect, useState} from "react";
import {Card, Button} from "react-bootstrap";
import BookDetails from "./BookDetails";
import BookSearch from "./BookSearch";

export default function Books(props) {

    const [books, setBooks] = useState([]);
    const [selectedBook, setSelectedBook] = useState(null);
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);

    useEffect(() => {
        fetch('https://localhost:8080/api/auth/book', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(result => result.json())
            .then(data => {
                console.log(data);
                setBooks(data);
            })
            .catch((error) => {
                console.log('Error:' + error);
            });
    }, [])


    const showBookDetails = (book) => {
       setSelectedBook(book);
       setShow(true);
    }

    return (
        <div className="row">
            <BookDetails show={show}
                         onHide={handleClose}
                         loggedIn={props.loggedIn}
                         book={selectedBook}
                         cartItems={props.cartItems}
                         setCartItems={props.setCartItems}
            />
            <div className="col-3">
                <BookSearch/>
            </div>
            <div className="books-preview col-9 row row-cols-4 text-center">
                { books?.map((book) => {
                    return (
                        <Card className="">
                            <Card.Header>
                                <Card.Img variant="top" src={"../images/books/" + book?.id + ".jpg"} />
                            </Card.Header>
                            <Card.Body>
                                <Card.Title>{book?.title}</Card.Title>
                                <Card.Text className="mb-0 font-weight-bold">Autor : {book?.writer?.firstName} {book?.writer?.lastName} </Card.Text>
                            </Card.Body>
                            <Card.Footer>
                                <h3 className="text-danger mb-5">{book?.price} $</h3>
                                <Button variant="outline-danger" className="w-100" onClick={() => showBookDetails(book)}>
                                    SHOW MORE DETAILS
                                </Button>
                            </Card.Footer>
                        </Card>
                    )
                })
                }
            </div>
        </div>
    );
}