import Vue from "vue";

const signIn = Vue.resource('/api/user/login');
const userBackup = Vue.resource('/api/user/backup');

export default {
    login: (login, password) => signIn.save({}, {
        login: login,
        password: password
    }),

    backup: () => userBackup.get()
}