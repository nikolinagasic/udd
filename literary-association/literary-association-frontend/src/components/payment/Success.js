import React, {useEffect} from "react";

export default function Success(props) {
    useEffect(() => {
        const route = window.location.href;
        const token = route?.split('?')[1]?.split('&')[0]?.split('=')[1];

        if (token !== undefined) {
            fetch('https://localhost:8081/pay-pal/capture/' + token, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    userId: 2,
                    merchantOrderId: 1,
                    merchantTimestamp: "2020-12-12 00:00:00",
                    amount: 100,
                    productId: 1,
                    buyerRequestId: 1,
                    username: "igor"
                })
            })
                .then(response => response.json())
                .catch((error) => console.error(error));
        }

        if (props?.match?.params?.request_id) {
            fetch('https://localhost:8080/api/auth/purchase-book/update/' + props.match.params.request_id, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: "PAYED"
            })
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                })
                .catch((error) => {
                    console.error('Error:', error);
                });
        }
    }, [props?.match?.params?.request_id]);

    return (
        <div className="bg-dark p-5 cart">
            <div className="bg-dark p-5 border border-light">
                <h2 className="text-left text-success mb-4">
                    Order Success
                </h2>
                <h4 className="bg-dark text-light text-left">Thank you! Your order has been successfully completed.</h4>
            </div>
        </div>
    );
}