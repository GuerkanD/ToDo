import {useState} from "react";
import {postLogin} from "../api/Api";

export default function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    function loginUser(e: React.FormEvent<HTMLFormElement>) {
        e.preventDefault();
        if (email === '' || password === '') {
            alert('Please fill in all fields');
            return;
        }
        postLogin(email, password).then((data) => {
            console.log(data);
        })
    }

    return (
        <>
            <div>
                <h1>Login</h1>
                <form onSubmit={(e) => loginUser(e)}>
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email"/>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password"/>
                    <button>Login</button>
                </form>
            </div>
        </>
    )
}