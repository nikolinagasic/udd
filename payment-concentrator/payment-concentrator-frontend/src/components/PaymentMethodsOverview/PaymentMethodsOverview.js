import React from "react";
import {Button, Table, Badge} from "react-bootstrap";
import CreatePaymentMethod from "./CreatePaymentMethod/CreatePaymentMethod";

export default function PaymentMethodsOverview(props) {

    return (<div className="Center mt-5 w-100">
        <CreatePaymentMethod create={props.create}/>
        {   props.methods &&
        <Table striped bordered hover variant="dark" responsive
               className="w-75">
            <thead>
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Delete</th>
            </tr>
            </thead>
            <tbody>
                {
                    props.methods.map((method, index) => {
                        return <tr key={method.id}>
                            <th>{index + 1}</th>
                            <th>{method.name}</th>
                            {method.id > 3 ?
                                <th><Button variant="danger" className="ml-1"
                                            onClick={() => props.delete(method.id)}>Delete</Button></th> :
                                <Badge pill variant="primary" className="ml-3 mt-2 h-100">Cannot delete</Badge>
                            }
                        </tr>
                    })
                }
            </tbody>
        </Table>
        }
    </div>);
}