import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useUser } from "./components/context/UserContext";
import { Link } from "react-router-dom";

const SignIn = () => {
  const url = "http://localhost:8080/api/user/authenticate";
  const navigate = useNavigate();
  const { login } = useUser(); // Get the login function from UserContext
  const [errors, setErrors] = useState({});
  const [user, setUser] = useState({
    username: "",
    password: "",
  });

  // Handle input changes
  const handleChange = (e) => {
    setUser({
      ...user,
      [e.target.name]: e.target.value,
    });
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(user),
      });

      if (response.ok) {
        const data = await response.json();

        login(data.user_id, data.jwt_token);

        navigate("/");
      } else {
        setErrors("Invalid username or password");
      }
    } catch (error) {
      setErrors("An error occurred. Please try again.");
    }
  };

  return (
    <div className="d-flex align-items-center justify-content-center">
      <div className="col-md-4">
        <h3 className="text-center">Sign In</h3>
        {errors & (errors.length > 0) ? (
          <div className="alert alert-danger">
            <p>The following errors were found:</p>
            <ul>
              {errors.map((error, index) => (
                <li key={index}>{error}</li>
              ))}
            </ul>
          </div>
        ) : null}
        <form onSubmit={handleSubmit} className="border p-4 shadow-sm">
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
          <button type="submit" className="btn btn-primary w-100">
            Sign In
          </button>
        </form>
        <div className="text-center mt-3">
          <Link to="/register">Don't have an account? Sign up!</Link>
        </div>
      </div>
    </div>
  );
};

export default SignIn;
