import Vue from "vue";
import VueResource from "vue-resource";

Vue.use(VueResource);
const signIn = Vue.resource('/user/login');

export default {
    login: (login, password) => signIn.save({}, {
        login: login,
        password: password
    })
}