import Vue from "vue";
import Message from "./Message.vue";
import Loader from "./Loader.vue";
import Dropdown from "./Dropdown.vue";
import List from "./List.vue";
import DateTimePicker from "./DateTimePicker.vue";
import HeadTitle from "./HeadTitle.vue";

Vue.component('app-message', Message);
Vue.component('app-loading', Loader);
Vue.component('app-dropdown', Dropdown);
Vue.component('app-list', List);
Vue.component('app-date-time-picker', DateTimePicker);
Vue.component('app-head-title', HeadTitle);