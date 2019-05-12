import Vue from "vue";
import 'util/resource'
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import store from "util/store.js";
import router from "util/router.js";
import App from "./App.vue";
import "components/components.js"

new Vue({
    el: '#app',
    store,
    router,
    render: a => a(App)
});