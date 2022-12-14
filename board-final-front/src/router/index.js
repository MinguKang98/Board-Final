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
import BoardPage from "@/views/boards/BoardPage";
import Boards from "@/views/boards/Boards";
import BoardWritePage from "@/views/boards/BoardWritePage";
import BoardModifyPage from "@/views/boards/BoardModifyPage";

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
        path: '/board/:type',
        name: 'Boards',
        component: Boards,
    },
    {
        path: '/board/:type/:boardSeq',
        name: 'BoardPage',
        component: BoardPage,
    },
    {
        path: '/:type/write',
        name: 'BoardWrite',
        component: BoardWritePage,
        beforeEnter: requireAuth()
    },
    {
        path: '/board/:type/:boardSeq/modify',
        name: 'BoardModify',
        component: BoardModifyPage,
        beforeEnter: requireAuth()
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
