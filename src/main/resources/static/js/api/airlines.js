import Vue from "vue";
import VueResource from "vue-resource";

Vue.use(VueResource);
const airlines = Vue.resource('/api/airlines{/id}');

export default {
    loadAirlines: () => airlines.get(),

    addAirline: (name) => airlines.save({}, {
        name: name
    }),

    deleteAirline: (id) => airlines.delete({id: id})
}