import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useUser } from "./components/context/UserContext"; // Import the useUser hook to access the login function

const SignIn = () => {
    const url = "http://localhost:8080/api/user/authenticate";
    const navigate = useNavigate();
    const { login } = useUser(); // Get the login function from UserContext
    const [errors, setErrors] = useState(null);
    const [user, setUser] = useState({
        username: "",
        password: ""
    });

    // Handle input changes
    const handleChange = (e) => {
        setUser({
            ...user,
            [e.target.name]: e.target.value
        });
    };

    // Handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(user)
            });

            if (response.ok) {
                const data = await response.json();
                
                // Use the login function from context to store user info in context and localStorage
                login(data.user_id, data.jwt_token); // Store userId and jwt_token in context

                // Navigate to home page after successful login
                navigate('/');
            } else {
                setErrors('Invalid username or password');
            }
        } catch (error) {
            setErrors('An error occurred. Please try again.');
        }
    };

    return (
        <div>
            <h2>Sign In</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Username:</label>
                    <input
                        type="text"
                        name="username"
                        value={user.username}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        name="password"
                        value={user.password}
                        onChange={handleChange}
                        required
                    />
                </div>
                {errors && <p style={{ color: 'red' }}>{errors}</p>}
                <button type="submit">Sign In</button>
            </form>
        </div>
    );
};

export default SignIn;
