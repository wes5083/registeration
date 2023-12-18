import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import axios from "axios";
import API_URL from "../config";

const Login = ({ onLogin }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const response = await axios.post(`${API_URL}/users/login`, {
        email,
        password,
      });
      let code = response.data.code;
      let msg = response.data.msg;
      if (code !== 1) {
        Swal.fire({
          icon: "warning",
          title: msg,
          showConfirmButton: false,
          timer: 3000,
        });
        return false;
      }

      const token = response.data.data.token;
      onLogin(token, email);
      navigate("/profile");
    } catch (error) {
      console.error("Login failed", error);
    }
  };

  return (
    <div>
      <h2>Login</h2>
      <p>
        <input
          type="text"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
      </p>
      <p>
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </p>
      <p>
        <button onClick={handleLogin}>Login</button>
      </p>
      <p className="forgot-password text-right">
        Want to create account <Link to="/register">register?</Link>
      </p>
    </div>
  );
};

export default Login;
