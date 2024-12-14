import {useState} from "react";
import {postLogin} from "../api/Api";
import {useNavigate} from "react-router-dom";

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
            <div>
                <h1>Login</h1>
                <form onSubmit={(e) => loginUser(e)}>
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email"/>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)}
                           placeholder="Password"/>
                    <button>Login</button>
                </form>
                <p>{errorMessage}</p>
            </div>
        </>
    )
}