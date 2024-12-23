import {useState} from "react";
import {postRegister} from "../api/Api";
import {useNavigate} from "react-router-dom";
import React from "react";

export default function Register() {

    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [passwordRepeat, setPasswordRepeat] = useState('');
    const navigator = useNavigate();
    function submitRegister(e: React.FormEvent) {
        e.preventDefault();
        if (password !== passwordRepeat) {
            alert('Passwords do not match');
            return;
        }
        postRegister(firstname, lastname, email, password).then((data) => {
            console.log(data);
            if (data.message !== 'User registered successfully!') {
                alert(`Register failed: ${data.message}`);
                return;
            }

            navigator('/login');
        });
    }

    return (<>
    <div>
        <h1>Register</h1>
        <form onSubmit={(e) => submitRegister(e)}>
            <input type="text" value={firstname} onChange={(e) => setFirstname(e.target.value)} placeholder="First Name" />
            <input type="text" value={lastname} onChange={(e) => setLastname(e.target.value)} placeholder="Last Name" />
            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" />
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" />
            <input type="password" value={passwordRepeat} onChange={(e) => setPasswordRepeat(e.target.value)} placeholder="Repeat Password" />
            <button>Register</button>
        </form>
    </div>
    </>);
}