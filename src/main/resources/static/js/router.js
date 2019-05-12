import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "./pages/Login.vue";
import Main from "./pages/Main.vue";

Vue.use(VueRouter);

const routes = [
    {path: '/login', component: Login},
    {path: '/', component: Main},
    {path: '*', component: Main}
];

export default new VueRouter({
    mode: 'history',
    routes
})