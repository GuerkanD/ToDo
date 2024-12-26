import { useState } from "react";
import { postLogin } from "../api/Api";
import { Link, useNavigate } from "react-router-dom";
import { ERROR_MESSAGES } from '../error/constants';
import React from "react";

export default function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('')
    const [errorEmail, setErrorEmail] = useState('')
    const [errorPassword, setErrorPassword] = useState('')
    const navigator = useNavigate();

    function loginUser(e: React.FormEvent<HTMLFormElement>) {
        e.preventDefault();
        
        setErrorMessage('')
        setErrorEmail('');
        setErrorPassword('')
        
        const fieldValidator = (): boolean => {
            let valid = true;
            if (email === '') {
                setErrorEmail(ERROR_MESSAGES.FIELD_EMPTY)
                valid = false;
            }
            if (password === '') {
                setErrorPassword(ERROR_MESSAGES.FIELD_EMPTY)
                valid = false;
            }
            return valid;
        }

        
        if (fieldValidator() === false) return;

        postLogin(email, password).then((data) => {
            if (data === 401) {
                setErrorMessage('Invalid email or password');
                return;
            }
            console.log(data);
            const token = data.message;
            localStorage.setItem('token', token);
            navigator('/');
        }).catch((e) => alert(`An unexpected error occured of type: ${e.message}`))
    }

    return (
        <>
            <div className="container">
                <div className="row justify-content-center align-items-center vh-100">
                    <div className="col-md-4 col-sm-6 border rounded">
                        <h1 className="text-center m-3">Login</h1>
                        <form onSubmit={(e) => loginUser(e)}>
                            <label>Enter your Email</label>
                            <input className="form-control" type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" />
                            <p className="text-danger">{errorEmail}</p>
                            <label className="mt-3">Please Enter your Password</label>
                            <input className="form-control" type="password" value={password} onChange={(e) => setPassword(e.target.value)}
                                placeholder="Password" />
                            <p className="text-danger">{errorPassword}</p>
                            <label>Don't have an Account? Make one <Link to="/register">here!</Link></label>
                            <div className="text-center">
                                <button className="btn btn-primary mb-3 col-12">Login</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </>
    )
}