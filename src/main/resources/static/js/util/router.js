import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "../pages/Login.vue";
import Main from "../pages/Main.vue";
import Airlines from "../pages/Airlines.vue";
import Error from "../components/Error.vue";
import Airplanes from "../pages/Airplanes.vue";
import Directions from "../pages/Directions.vue";

Vue.use(VueRouter);

const routes = [
    {path: '/login', component: Login},
    {path: '/airlines', component: Airlines},
    {path: '/airplanes', component: Airplanes},
    {path: '/directions', component: Directions},
    {path: '/', component: Main},
    {path: '*', component: Error}
];

export default new VueRouter({
    mode: 'history',
    routes
})