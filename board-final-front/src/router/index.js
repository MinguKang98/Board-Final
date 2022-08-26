import Vue from 'vue'
import VueRouter from 'vue-router'
import HomePage from '../views/HomePage.vue'
import LoginPage from "@/views/users/LoginPage";
import {getTokenFromCookie, getUserRoleFromCookie} from "@/utils/cookie";
import UserDetailPage from "@/views/users/UserDetailPage";
import NotFound from "@/views/NotFound";
import RegisterPage from "@/views/users/RegisterPage";
import UserModifyPage from "@/views/users/UserModifyPage";
import UserDeletePage from "@/views/users/UserDeletePage";
import NotifyBoards from "@/views/boards/NotifyBoards";
import NotifyBoardPage from "@/views/boards/NotifyBoardPage";
import NotifyBoardWritePage from "@/views/boards/NotifyBoardWritePage";

Vue.use(VueRouter)

const requireAuth = () => (from, to, next) => {
    const token = getTokenFromCookie();
    if (token) {
        return next()
    }
    next('/login')
}

const requireUnAuth = () => (from, to, next) => {
    const token = getTokenFromCookie();
    if (token) {
        return next('/')
    }
    return next()
};

const requireAdmin = () => (from, to, next) => {
    const token = getUserRoleFromCookie();
    if (token === 'ROLE_ADMIN') {
        return next()
    }
    return next('/')
};

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomePage
    },
    {
        path: '/login',
        name: 'login',
        component: LoginPage,
        beforeEnter: requireUnAuth()
    },
    {
        path: '/user/:userSeq',
        name: 'userDetail',
        component: UserDetailPage,
    },
    {
        path: '/register',
        name: 'register',
        component: RegisterPage,
        beforeEnter: requireUnAuth()
    },
    {
        path: '/modify',
        name: 'userModify',
        component: UserModifyPage,
        beforeEnter: requireAuth()
    },
    {
        path: '/delete',
        name: 'userDelete',
        component: UserDeletePage,
        beforeEnter: requireAuth()
    },
    {
        path: '/board/notify',
        name: 'notifyBoard',
        component: NotifyBoards,
    },
    {
        path: '/board/notify/:boardSeq',
        name: 'notifyBoardPage',
        component: NotifyBoardPage,
    },
    {
        path: '/notify/write',
        name: 'notifyBoardWrite',
        component: NotifyBoardWritePage,
        beforeEnter: requireAdmin()
    },
    {
        path: '*',
        redirect: '/404',
    },
    {
        path: '/404',
        component: NotFound,
    }
]

const router = new VueRouter({
    routes
})

export default router
