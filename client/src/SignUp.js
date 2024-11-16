import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useUser } from "./components/context/UserContext"; 

const DEFAULT_USER = {
  firstName: "",
  lastName: "",
  username: "",
  email: "",
  password: "",
  confirmPassword: "",
};


function SignUp() {
  const [user, setUser] = useState(DEFAULT_USER);
  const [errors, setErrors] = useState({});
  
  const { login } = useUser(); // Get the login function from UserContext
  const baseUrl = process.env.NODE_ENV === "production" ? "https://stockconnect.onrender.com" : "http://localhost:8080";
  const url = `${baseUrl}/api/user/signup`;
  const authUrl = `${baseUrl}/api/user/authenticate`;
  const navigate = useNavigate();

  const handleChange = (e) => {
    const newUser = { ...user };
    newUser[e.target.name] = e.target.value;
    setUser(newUser);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    addUser();
  }

  const authenticateUser = async () => {
    const authUser = {
      username: user.username,
      password: user.password
    }
    const init = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(authUser),
    }

    const response = await fetch(authUrl, init);
    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("jwt_token", data.jwt_token);
      login(data.user_id, data.jwt_token)
      navigate("/");
    } else {
      setErrors({ message: "Invalid username or password" });
    }
  }

  const addUser = async () => {
    const init = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
    };

    const response = await fetch(url, init);
    if (response.ok) {
      const data = await response.json();
      if (data) {
        await authenticateUser();
      } 
    } else {
      const data = await response.json();
      updateErrorText(data);
    }
  };

  const updateErrorText = (errors) => {
    const errorMessages = {
      username: "Username",
      email: "Email",
      password: "Password",
      firstName: "First Name",
      lastName: "Last Name",
    };

    const newErrors = errors.map((error) => {
      for (const key in errorMessages) {
        if (error.includes(key)) {
          return error.replace(key, errorMessages[key]);
        }
      }
      return error;
    });
    setErrors(newErrors);
  };

  return (
    <div className="d-flex align-items-center justify-content-center">
      <div className="col-md-4">
        <h3 className="text-center">Sign Up</h3>
        {errors.length && (
        <div className="alert alert-danger">
          <p>The following Errors were found:</p>
          <ul>
            {errors.map((error) => (
              <li key={error}>{error}</li>
            ))}
          </ul>
        </div>
      )}
        <form onSubmit={handleSubmit} className="border p-4 shadow-sm">
          <div className="mb-3">
            <label htmlFor="firstName" className="form-label">
              First Name
            </label>
            <input
              type="text"
              className="form-control"
              id="firstName"
              name="firstName"
              placeholder="Enter your first name"
              value={user.firstName}
              onChange={handleChange}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="lastName" className="form-label">
              Last Name
            </label>
            <input
              type="text"
              className="form-control"
              id="lastName"
              name="lastName"
              placeholder="Enter your last name"
              value={user.lastName}
              onChange={handleChange}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="username" className="form-label">
              Username
            </label>
            <input
              type="text"
              className="form-control"
              id="username"
              name="username"
              placeholder="Enter your username"
              value={user.username}
              onChange={handleChange}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="email" className="form-label">
              Email
            </label>
            <input
              type="email"
              className="form-control"
              id="email"
              name="email"
              placeholder="Enter your email"
              value={user.email}
              onChange={handleChange}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="password" className="form-label">
              Password
            </label>
            <input
              type="password"
              className="form-control"
              id="password"
              name="password"
              placeholder="Enter your password"
              value={user.password}
              onChange={handleChange}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="confirmPassword" className="form-label">
              Confirm Password
            </label>
            <input
              type="password"
              className="form-control"
              id="confirmPassword"
              name="confirmPassword"
              placeholder="Confirm your password"
              value={user.confirmPassword}
              onChange={handleChange}
              required
            />
          </div>
          <button type="submit" className="btn btn-primary w-100">
            Sign Up
          </button>
        </form>
        <div className="text-center mt-3">
          <Link to="/login">Already have an account? Log in</Link>
        </div>
      </div>
    </div>
  );
}

export default SignUp;
