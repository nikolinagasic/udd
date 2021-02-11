import {Button, Modal, Table} from "react-bootstrap";
import React, {useEffect, useState} from "react";

export default function Users(props) {

    const [users, setUsers] = useState([]);

    useEffect(() => {
        fetch("https://localhost:8081/user", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + JSON.parse(localStorage.getItem("token"))
            }
        })
            .then(response => response.json())
            .then(data => {
                setUsers(data);
            })
            .catch((error) => {
                console.error("Error", error);
            })
    }, [])

    const deactivateUser = (userId) => {
        fetch("https://localhost:8081/user/deactivate/" + userId, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + JSON.parse(localStorage.getItem("token"))
            }
        })
            .then(response => window.location.reload())
            .catch((error) => {
                console.error("Error", error);
            })
    }

    const activateUser = (userId) => {
        fetch("https://localhost:8081/user/activate/" + userId, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + JSON.parse(localStorage.getItem("token"))
            }
        })
            .then(response => window.location.reload())
            .catch((error) => {
                console.error("Error", error);
            })
    }

    return (<div className="Center mt-5 w-100">
        <Table striped bordered hover variant="dark" responsive
               className="w-75">
            <thead>
            <tr>
                <th>#</th>
                <th>Type</th>
                <th>Username</th>
                <th>Created at</th>
                <th>Updated at</th>
                <th>Last login</th>
                <th>Status</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            {users.map((user, index) => {
                return <tr key={user.id}>
                    <th>{index + 1}</th>
                    <th>{user.type}</th>
                    <th>{user.username}</th>
                    <th>{user.createdAt}</th>
                    <th>{user.updatedAt}</th>
                    <th>{user.lastLogin}</th>
                    <th>{user.active}</th>
                    {   (user.active === "ACTIVE" && user.type !== "ADMIN") &&
                        <th><Button variant="danger" onClick={() => deactivateUser(user.id)}>DEACTIVATE</Button></th>
                    }
                    {   (user.active === "SUSPENDED" && user.type !== "ADMIN") &&
                        <th><Button variant="success" onClick={() => activateUser(user.id)}>ACTIVATE</Button></th>
                    }
                </tr>
            })}
            </tbody>
        </Table>
    </div>);

}