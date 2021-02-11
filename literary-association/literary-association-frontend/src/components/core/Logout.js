import React from "react";

export default function Logout(props) {

    const logout = () => {
        props.logout();
    }

    return (
        <span className={props.className} onClick={() => logout()}>
            Logout
        </span>
    )
}