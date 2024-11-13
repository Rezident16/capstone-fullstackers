import { useState } from "react";
import { useNavigate } from "react-router-dom";

const SignIn = () => {
    const url = "http://localhost:8080/api/user/authenticate";
    const navigate = useNavigate();
    const [errors, setErrors] = useState(null);
    const [user, setUser] = useState({
        username: "",
        password: ""
    });

    const handleChange = (e) => {
        setUser({
            ...user,
            [e.target.name]: e.target.value
        });
    };

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
                localStorage.setItem('jwt_token', data.jwt_token);
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
