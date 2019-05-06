import Vue from "vue";

const airplanes = Vue.resource('/api/airplanes{/id}');

export default {
    loadAirplanes: () => airplanes.get(),

    addAirplane: (model, capacity, airlineId) => airlines.save({}, {
        model: model,
        capacity: capacity,
        airlineId: airlineId
    }),

    deleteAirplane: (id) => airlines.delete({id: id})
}