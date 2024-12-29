import { ERROR_MESSAGES } from '../error/constants';


const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/;

export const isPasswordValid = (password: string) => {
    if (password === '') return ERROR_MESSAGES.FIELD_EMPTY;
    if (password.length < 8) return ERROR_MESSAGES.PASSWORD_SHORT;
    if (password.length > 72) return ERROR_MESSAGES.PASSWORD_LONG;

    if (!passwordRegex.test(password)) return ERROR_MESSAGES.NOT_SAFE_PASSWORD;
        
    return true;
}