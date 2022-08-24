import {getUser, login} from "@/api/user";
import {
    clearAllCookies,
    getTokenFromCookie, getUserIdFromCookie, getUserRoleFromCookie,
    getUserSeqFromCookie,
    saveTokenToCookie,
    saveUserIdToCookie, saveUserRoleToCookie,
    saveUserSeqToCookie
} from "@/utils/cookie";


const userStore = {
    namespaced: true,
    state: {
        token: getTokenFromCookie() || '',
        userSeq: getUserSeqFromCookie() || '',
        userId: getUserIdFromCookie() || '',
        userRole: getUserRoleFromCookie() || ''
    },
    getters: {
        isLogin(state) {
            return state.token !== '';
        },
        isAuthorized(state) {
            return (userSeq) => {
                return (state.userSeq === userSeq) || (state.userRole === 'ROLE_ADMIN');
            }
        },
        getToken(state) {
            return state.token;
        },
        getUserSeq(state) {
            return state.userSeq;
        },
        getUserId(state) {
            return state.userId;
        },
        getUserRole(state) {
            return state.userRole;
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
        setUserRole(state, userRole) {
            state.userRole = userRole;
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
            commit('setUserRole', userResponse.data.role);

            saveTokenToCookie(tokenResponse.data.token);
            saveUserSeqToCookie(tokenResponse.data.userSeq);
            saveUserIdToCookie(userResponse.data.id);
            saveUserRoleToCookie(userResponse.data.role);
        },
        logout({commit}) {
            commit('clearAll');
        },
    },
};

export default userStore;
