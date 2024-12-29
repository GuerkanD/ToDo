
import { ERROR_MESSAGES } from '../error/constants';

const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

export const isEmailValid = (email: string) => {
    if (email === '') return ERROR_MESSAGES.FIELD_EMPTY;
    if (!emailRegex.test(email)) return ERROR_MESSAGES.NOT_AN_EMAIL;

    return true;
}