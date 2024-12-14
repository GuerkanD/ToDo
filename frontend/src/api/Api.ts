import StatusCodes from 'http-status-codes'

const API_ROUTE = 'http://localhost:8080';

export const postLogin = async (email: string, password: string) => {
    const response = await fetch(`${API_ROUTE}/api/v1/login`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
    });
    if (response.status == StatusCodes.UNAUTHORIZED) return StatusCodes.UNAUTHORIZED;
    return response.json();
}

export const postRegister = async (firstname: string, lastname:string, email: string, password: string) => {
    const response = await fetch(`${API_ROUTE}/api/v1/register`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify({ firstname,lastname ,email, password }),
    });
    return response.json();
}