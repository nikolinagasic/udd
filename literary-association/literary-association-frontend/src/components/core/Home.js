import React, {useState} from "react";
import {Carousel} from "react-bootstrap";
import Login from "./Login.js";
import Logout from "./Logout";

export default function Home(props) {

    return (
        <div className="row ml-4 mr-5">
            <Carousel className={"adjust-carousel col-6 h-100"}>
                <Carousel.Item interval={4000}>
                    <img
                        className="d-block w-100"
                        src={"../images/c1.jpg"}
                        alt="First slide"
                    />
                    <Carousel.Caption>
                        <h3>Neil Gaiman</h3>
                        <p>A book is a dream that you hold in your hand.</p>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item interval={4000}>
                    <img
                        className="d-block w-100"
                        src={"../images/c2.jpg"}
                        alt="Second slide"
                    />
                    <Carousel.Caption>
                        <h3>Stephen King</h3>
                        <p>Books are a uniquely portable magic.</p>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item interval={4000}>
                    <img
                        className="d-block w-100"
                        src={"../images/c3.jpg"}
                        alt="Third slide"
                    />
                    <Carousel.Caption>
                        <h3>George R.R. Martin</h3>
                        <p>A reader lives a thousand lives before he dies.</p>
                    </Carousel.Caption>
                </Carousel.Item>
            </Carousel>
            {   !props.loggedIn &&
                <div className="col-5 offset-1 content bg-dark h-75">
                    <Login login={props.login}/>
                </div>
            }
            {   props.loggedIn &&
            <div className="col-5 offset-1 content bg-dark h-75">
                <div className="m-5 custom-form border-light border pb-5">
                    <h1 className="mt-5 mb-5">Welcome!</h1>
                    <p>You are logged in as ...</p>
                    <Logout className="btn btn-primary" logout={props.logout}/>
                </div>
            </div>
            }
        </div>
    );
}