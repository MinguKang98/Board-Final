import {instance, instanceWithAuth} from "@/api/index";

function login(loginData) {
    console.log(loginData);
    return instance.post('/api/login', loginData);
}

function register(registerData) {
    return instance.post('/api/users', registerData);
}

function getUser(userSeq) {
    return instance.get(`/api/users/${userSeq}`);
}

function changePassword(userSeq, passwordData) {
    return instanceWithAuth.put(`/api/users/${userSeq}`, passwordData);
}

function deleteUser(userSeq) {
    return instanceWithAuth.delete(`/api/users/${userSeq}`);
}

function idDuplicatedCheck(userId) {
    return instance.post('/api/users/id', userId);
}

function emailDuplicatedCheck(email) {
    return instance.post('/api/users/email', email);
}

function banUser(userSeq) {
    return instanceWithAuth.put(`/api/users/${userSeq}/ban`);
}

function unbanUser(userSeq) {
    return instanceWithAuth.put(`/api/users/${userSeq}/unban`);
}

export {
    login,
    register,
    getUser,
    changePassword,
    deleteUser,
    idDuplicatedCheck,
    emailDuplicatedCheck,
    banUser,
    unbanUser
}
