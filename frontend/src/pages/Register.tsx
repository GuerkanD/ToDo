import { useState } from "react";
import { postRegister } from "../api/Api";
import { Link, useNavigate } from "react-router-dom";
import { ERROR_MESSAGES } from '../error/constants';
import React from "react";

export default function Register() {

    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [passwordRepeat, setPasswordRepeat] = useState('');
    const navigator = useNavigate();

    const [lastnameError, setLastnameError] = useState('')
    const [firstnameError,setFirstnameError] = useState('')
    const [emailError, setEmailError] = useState('')
    const [passwordError, setPasswordError] = useState('')
    const [passwordRepeatError, setPasswordRepeatError] = useState('')
    

    function submitRegister(e: React.FormEvent) {
        e.preventDefault();
        setFirstnameError(''); 
        setLastnameError('');
        setEmailError('');
        setPasswordError('');

        const validEntries = (): boolean => {
            let valid = true;
            if (firstname === ''){
                setFirstnameError(ERROR_MESSAGES.FIELD_EMPTY)
                valid = false;
            }
            if (lastname === ''){
                setLastnameError(ERROR_MESSAGES.FIELD_EMPTY)
                valid = false;
            }
            if (email === '') {
                setEmailError(ERROR_MESSAGES.FIELD_EMPTY)
            }
            if (password === ''){
                setPasswordError(ERROR_MESSAGES.FIELD_EMPTY)
                valid = false;
            }
            if (passwordRepeat === ''){
                setPasswordRepeatError(ERROR_MESSAGES.FIELD_EMPTY)
                valid = false;
            }
            if ((password !== passwordRepeat) && (password !== '') && (passwordRepeat !== '')) {
                setPasswordError(ERROR_MESSAGES.UNMATCHING_PASSWORD)
                setPasswordRepeatError(ERROR_MESSAGES.UNMATCHING_PASSWORD)
                valid = false;
            }
            return valid;
        }

        if(validEntries() === false) return;

        postRegister(firstname, lastname, email, password).then((data) => {
            console.log(data);
            if (data.message !== 'User registered successfully!') {
                alert(`Register failed: ${data.message}`);
                return;
            }

            navigator('/login');
        }).catch((e) => alert(`An unexpected error occured of type: ${e.message}`));
    }

    return (<>
        <div className="container">
            <div className="row justify-content-center align-content-center vh-100">
                <div className="col-md-7 col-sm-6 border rounded">
                    <h1 className="text-center m-3">Register</h1>
                    <form onSubmit={(e) => submitRegister(e)}>
                        <div className="row my-3">
                            <div className="col-md-6">
                                <label>Enter your First Name</label>
                                <input className="form-control" type="text" value={firstname} onChange={(e) => setFirstname(e.target.value)} placeholder="First Name" />
                            </div>
                            <div className="col-md-6">
                                <label>Enter your Last Name</label>
                                <input className="form-control" type="text" value={lastname} onChange={(e) => setLastname(e.target.value)} placeholder="Last Name" />
                            </div>
                        </div>
                        <div>
                            <label>Enter your Email</label>
                            <input className="form-control" type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" />
                        </div>
                        <div>
                            <label>Enter your Password</label>
                            <input className="form-control" type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" />
                        </div>
                        <div>
                            <label>Repeat your Password</label>
                            <input className="form-control" type="password" value={passwordRepeat} onChange={(e) => setPasswordRepeat(e.target.value)} placeholder="Repeat Password" />
                        </div>
                        <div className="my-3">
                            <button className="btn btn-primary col-12">Register</button>
                            <label>Already have an Account? Press <Link to="/login">here</Link> to log in</label>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </>);
}