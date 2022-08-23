import {getUser, login} from "@/api/user";
import {
    clearAllCookies,
    getTokenFromCookie, getUserIdFromCookie,
    getUserSeqFromCookie,
    saveTokenToCookie,
    saveUserIdToCookie,
    saveUserSeqToCookie
} from "@/utils/cookie";


const userStore = {
    namespaced: true,
    state: {
        token: getTokenFromCookie() || '',
        userSeq: getUserSeqFromCookie() || '',
        userId: getUserIdFromCookie() || '',
    },
    getters: {
        getToken(state) {
            return state.token;
        },
        getUserSeq(state) {
            return state.userSeq;
        },
        getUserId(state) {
            return state.userId;
        },
    },
    mutations: {
        setToken(state, token) {
            state.token = token;
        },
        setUserSeq(state, userSeq) {
            state.userSeq = userSeq;
        },
        setUserId(state, userId) {
            state.userId = userId;
        },
        clearAll(state) {
            state.token = '';
            state.userSeq = '';
            state.userId = '';

            clearAllCookies();
        },
    },
    actions: {
        async login({commit}, loginData) {
            const tokenResponse = await login(loginData);
            const userResponse = await getUser(tokenResponse.data.userSeq)

            commit('setToken', tokenResponse.data.token);
            commit('setUserSeq', tokenResponse.data.userSeq);
            commit('setUserId', userResponse.data.id);

            saveTokenToCookie(tokenResponse.data.token);
            saveUserSeqToCookie(tokenResponse.data.userSeq);
            saveUserIdToCookie(userResponse.data.id);
        },
        logout({commit}) {
            commit('clearAll');
        },
    },
};

export default userStore;
