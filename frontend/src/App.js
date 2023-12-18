import React, { useState } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Swal from "sweetalert2";
import axios from "axios";
import Register from "./finnplay/Register";
import Login from "./finnplay/Login";
import UserProfile from "./finnplay/UserProfile";
import API_URL from "./config";

const App = () => {
  const [token, setToken] = useState(localStorage.getItem("token"));

  const handleLogin = (newToken, email) => {
    setToken(newToken);
    localStorage.setItem("token", newToken);
    localStorage.setItem("email", email);
  };

  const handleLogout = async () => {
    try {
      const response = await axios.post(`${API_URL}/users/logout`);
      let code = response.data.code;
      let msg = response.data.msg;
      if (code !== 1) {
        Swal.fire({
          icon: "warning",
          title: msg,
          showConfirmButton: false,
          timer: 1000,
        });
        return false;
      }
      setToken(null);
      localStorage.removeItem("token");
      localStorage.removeItem("email");
    } catch (error) {
      console.error("Login failed", error);
    }
  };

  return (
    <Router>
      <Routes>
        <Route
          path="/"
          element={
            token ? <Navigate to="/profile" /> : <Navigate to="/login" />
          }
        />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login onLogin={handleLogin} />} />
        <Route
          path="/profile"
          element={<UserProfile token={token} onLogout={handleLogout} />}
        />
      </Routes>
    </Router>
  );
};

export default App;
