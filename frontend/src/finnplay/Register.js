import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import axios from "axios";
import API_URL from "../config";

const Register = ({ history }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleRegister = async () => {
    try {
      const response = await axios.post(`${API_URL}/users/register`, {
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

      navigate("/login");
    } catch (error) {
      console.error("Registration failed", error);
    }
  };

  return (
    <div>
      <h2>Register</h2>
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
        <button onClick={handleRegister}>Register</button>
      </p>

      <p className="forgot-password text-right">
        Already registered <Link to="/login">log in?</Link>
      </p>
    </div>
  );
};

export default Register;
