import React, {useEffect} from "react";

export default function Failed(props) {
    useEffect(() => {
        if (props?.match?.params?.request_id) {
            fetch('https://localhost:8080/api/auth/purchase-book/update/' + props.match.params.request_id, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: "FAILED"
            })
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                })
                .catch((error) => {
                    console.error('Error:', error);
                });
        }
    }, [])

    return(
        <div className="bg-dark p-5 cart">
            <div className="bg-dark p-5 border border-light">
                <h2 className="text-left text-danger mb-4">
                    Order Failed
                </h2>
                <h4 className="bg-dark text-light text-left">Your order request failed. Please try again.</h4>
            </div>
        </div>
    );
}