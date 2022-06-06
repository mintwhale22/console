
export const isPassword = (value) => {
    const regex = /(?=.*\d{1,20})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,20}).{8,20}$/;
    return regex.test(value);
}

export const isEmail = (value) => {
    const regex = /^(([^<>()\[\].,;:\s@"]+(\.[^<>()\[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i;
    return regex.test(value);
};