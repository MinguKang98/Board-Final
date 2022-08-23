import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginPage from "@/views/users/LoginPage";
import {getTokenFromCookie} from "@/utils/cookie";

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

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/about',
        name: 'about',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
    },
    {
        path: '/login',
        name: 'login',
        component: LoginPage,
        beforeEnter : requireUnAuth()
    },
]

const router = new VueRouter({
    routes
})

export default router
