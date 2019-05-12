import Vue from "vue";

const signIn = Vue.resource('/api/user/login');

export default {
    login: (login, password) => signIn.save({}, {
        login: login,
        password: password
    })
}