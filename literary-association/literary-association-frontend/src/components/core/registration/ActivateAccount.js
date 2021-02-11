import React, {useEffect} from "react";

export default function ActivateAccount(props) {

    useEffect(() => {
        const route = window.location.href;
        const userId = route.split('?')[1].split('&')[0].split('=')[1];
        console.log('token je ' + userId);

        fetch('https://localhost:8080/api/auth/registration/activate-user/' + userId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => response.json())
            .catch((error) => console.error(error));
    });

    return (
        <div className="col-4 content bg-dark p-1">
            <div className="m-5 custom-form border-light border pb-5">
                <h4 className="text-justify text-success p-3 mt-5">
                    You have successfully activated your account.
                </h4>
            </div>
        </div>
    );
}