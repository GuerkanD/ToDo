import { useState } from "react";
import { postLogin } from "../api/Api";
import { useNavigate } from "react-router-dom";
import React from "react";

export default function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigator = useNavigate();

    function loginUser(e: React.FormEvent<HTMLFormElement>) {
        e.preventDefault();
        setErrorMessage('');
        if (email === '' || password === '') {
            alert('Please fill in all fields');
            return;
        }
        postLogin(email, password).then((data) => {
            if (data === 401) {
                setErrorMessage('Invalid email or password');
                return;
            }
            console.log(data);
            const token = data.message;
            localStorage.setItem('token', token);
            navigator('/');
        })
    }

    return (
        <>
            <div className="container">
                <div className="row justify-content-center align-items-center vh-100">
                    <div className="col-md-4 col-sm-6">
                        <h1 className="text-center">Login</h1>
                        <form onSubmit={(e) => loginUser(e)}>
                            <label>Enter your Email</label>
                            <input className="form-control" type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" />
                            <label>Please Enter your Password</label>
                            <input className="form-control" type="password" value={password} onChange={(e) => setPassword(e.target.value)}
                                placeholder="Password" />
                            <div>
                                <button className="btn">Login</button>
                            </div>
                        </form>
                    </div>
                    <p>{errorMessage}</p>
                </div>
            </div>
        </>
    )
}