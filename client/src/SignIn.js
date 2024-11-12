import React, { useState } from 'react';
import { Link } from "react-router-dom";

const SignIn = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Username:', username);
        console.log('Password:', password);
    };

    return (
        <div className="d-flex align-items-center justify-content-center vh-100">
            <div className="col-md-4">
                <h3 className="text-center">Sign In</h3>
                <form onSubmit={handleSubmit} className="border p-4 shadow-sm">
                    <div className="mb-3">
                        <label htmlFor="username" className="form-label">Username</label>
                        <input
                            type="text"
                            className="form-control"
                            id="username"
                            placeholder="Enter your username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="password" className="form-label">Password</label>
                        <input
                            type="password"
                            className="form-control"
                            id="password"
                            placeholder="Enter your password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    {/*
                    <button type="submit" className="btn btn-primary w-100">Sign In</button>
                    */}
                    
                    <Link to="/" className="btn btn-primary w-100">
                        Sign In
                    </Link>
                </form>
                <div className="text-center mt-3">
                    <span>Don't have an account? <Link to="/register">Sign Up here</Link></span>
                </div>
            </div>
        </div>
    );
};

export default SignIn;
