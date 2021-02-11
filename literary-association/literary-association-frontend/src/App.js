import React, {useState} from "react";

import "./styles/app.scss";
import NavigationBar from "./components/core/NavigationBar";
import Routes from "./Routes";
import UseLocalStorage from "./UseLocalStorage";

function App() {

    const [loggedIn, setLoggedIn] = UseLocalStorage("token", null);
    const [roles, setRoles] = UseLocalStorage("roles", null);
    const [cartItems, setCartItems] = UseLocalStorage("cartItems", []);

    const logout = () => {
        setLoggedIn(null);
        setRoles([]);
    }

    const login = (token, roles) => {
        setLoggedIn(token);
        setRoles(roles);
    }

      return (
        <div className="app">
          <NavigationBar cartItemsLength={cartItems.length} loggedIn={loggedIn} logout={logout} roles={roles}/>
          <div className="container-fluid mb-5">
              <Routes loggedIn={loggedIn}
                      login={login}
                      logout={logout}
                      roles={roles}
                      cartItems={cartItems}
                      setCartItems={setCartItems}/>
          </div>
        </div>
      );
}

export default App;
