import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Swal from "sweetalert2";
import API_URL from "../config";

const UserProfile = ({ token, onLogout }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [birthDay, setBirthDay] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    if (token === null || token === undefined) {
      navigate("/login");
    } else {
      const fetchUserProfile = async () => {
        try {
          const response = await axios.get(
            `${API_URL}/users/` + localStorage.getItem("email"),
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );

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
          setEmail(response.data.data.email);
          setFirstName(response.data.data.firstName);
          setLastName(response.data.data.lastName);
          setBirthDay(response.data.data.birthDay);
        } catch (error) {
          console.error("Failed to fetch user profile", error);
        }
      };

      fetchUserProfile();
    }
  }, [token]);

  const handleLogout = () => {
    try {
      onLogout();
    } catch (error) {
      console.error("logout exception", error);
    }
    navigate("/login");
  };

  const handleSaveUser = async () => {
    try {
      const response = await axios.put(
        `${API_URL}/users/` + email,
        {
          email,
          password,
          firstName,
          lastName,
          birthDay,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

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

      Swal.fire({
        icon: "success",
        title: "saved successfully!",
        showConfirmButton: false,
        timer: 3000,
      });
    } catch (error) {
      console.error("Failed to save user profile", error);
    }
  };

  return (
    <div>
      <h2>User Profile</h2>

      <p>
        Email: <input type="text" placeholder="Email" value={email} readOnly />
      </p>
      <p>
        Password:{" "}
        <input
          type="text"
          placeholder="Password"
          value={password || ""}
          onChange={(e) => setPassword(e.target.value)}
        />
      </p>
      <p>
        FirstName:{" "}
        <input
          type="text"
          placeholder="FirstName"
          value={firstName || ""}
          onChange={(e) => setFirstName(e.target.value)}
        />
      </p>
      <p>
        LastName:{" "}
        <input
          type="text"
          placeholder="LastName"
          value={lastName || ""}
          onChange={(e) => setLastName(e.target.value)}
        />
      </p>
      <p>
        BirthDay:{" "}
        <input
          type="date"
          placeholder="BirthDay"
          value={birthDay || ""}
          onChange={(e) => setBirthDay(e.target.value)}
        />
      </p>
      <button onClick={handleSaveUser}>Save User</button>

      <button onClick={handleLogout}>Logout</button>
    </div>
  );
};

export default UserProfile;
