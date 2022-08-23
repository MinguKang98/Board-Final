function saveTokenToCookie(value) {
    document.cookie = `token=${value}`;
}

function saveUserSeqToCookie(value) {
    document.cookie = `userSeq=${value}`;
}

function saveUserIdToCookie(value) {
    document.cookie = `userId=${value}`;
}

function getTokenFromCookie() {
    return document.cookie.replace(
        /(?:(?:^|.*;\s*)token\s*=\s*([^;]*).*$)|^.*$/,
        '$1',
    );
}

function getUserSeqFromCookie() {
    return document.cookie.replace(
        /(?:(?:^|.*;\s*)userSeq\s*=\s*([^;]*).*$)|^.*$/,
        '$1',
    );
}

function getUserIdFromCookie() {
    return document.cookie.replace(
        /(?:(?:^|.*;\s*)userId\s*=\s*([^;]*).*$)|^.*$/,
        '$1',
    );
}

function deleteCookie(value) {
    document.cookie = `${value}=; expires=Thu, 01 Jan 1970 00:00:01 GMT;`;
}

function clearAllCookies() {
    deleteCookie('token');
    deleteCookie('userSeq');
    deleteCookie('userId');
}

export {
    saveTokenToCookie,
    saveUserSeqToCookie,
    saveUserIdToCookie,
    getTokenFromCookie,
    getUserSeqFromCookie,
    getUserIdFromCookie,
    deleteCookie,
    clearAllCookies
};