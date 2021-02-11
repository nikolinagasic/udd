import React, {useState} from "react";
import "./App.css";
import Navbar from "./components/UI/Navbar/Navbar";
import Routes from "./Routes"

export default function App() {
    const [loggedIn, setLoggedIn] = useLocalStorage("token", null);
    const [roles, setRoles] = useLocalStorage("roles", null);

    const log = (value, role) => {
        setLoggedIn(value);
        setRoles(role);
    }

    return (
        <div>
            <Navbar loggedIn={loggedIn} log={log}/>
            <Routes loggedIn={loggedIn} log={log} roles={roles}/>
        </div>
    );
}

// Hook
function useLocalStorage(key, initialValue) {
    // State to store our value
    // Pass initial state function to useState so logic is only executed once
    const [storedValue, setStoredValue] = useState(() => {
        try {
            // Get from local storage by key
            const item = window.localStorage.getItem(key);
            // Parse stored json or if none return initialValue
            return item ? JSON.parse(item) : initialValue;
        } catch (error) {
            // If error also return initialValue
            console.log(error);
            return initialValue;
        }
    });

    // Return a wrapped version of useState's setter function that ...
    // ... persists the new value to localStorage.
    const setValue = value => {
        try {
            // Allow value to be a function so we have same API as useState
            const valueToStore =
                value instanceof Function ? value(storedValue) : value;
            // Save state
            setStoredValue(valueToStore);
            // Save to local storage
            window.localStorage.setItem(key, JSON.stringify(valueToStore));
        } catch (error) {
            // A more advanced implementation would handle the error case
            console.log(error);
        }
    };

    return [storedValue, setValue];
}