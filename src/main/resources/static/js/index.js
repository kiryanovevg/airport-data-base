import Vue from "vue";
import 'api/resource'
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import router from "./router.js";
import App from "./App.vue";

new Vue({
    el: '#app',
    router,
    render: a => a(App)
});