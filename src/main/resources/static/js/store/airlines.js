import apiAirlines from "../api/airlines";
import response from "vue-resource/src/http/response";


export default {
    state: {
        data: []
    },

    getter: {

    },

    mutations: {
        updateAirlinesMutation(state, airlines) {
            state.data = airlines;
        },

        addAirlineMutation(state, airline) {
            state.data.push(airline)
        },

        removeAirlineMutation(state, id) {
            let airline;

            state.data.forEach(item => {
                if (item.id === id) airline = item;
            });

            if (airline !== null) {
                const index = state.data.indexOf(airline);
                if (index > -1) state.data.splice(index, 1);
            } else throw 'Airline not found with id: ' + id;
        }
    },

    actions: {
        updateAirlinesAction({ commit, state }, ui) {
            if (ui !== undefined) {
                ui(true, null);

                apiAirlines.loadAirlines().then(
                    response => {
                        commit('updateAirlinesMutation', response.body);
                        ui(false, null);
                    },

                    error => {
                        ui(false, error.bodyText)
                    }
                )
            } else throw 'Not setted ui callback';
        },

        addAirlineAction({ commit, state }, { name, ui, success }) {
            if (ui !== undefined) {
                ui(true, null);

                apiAirlines.addAirline(name).then(
                    response => {
                        commit('addAirlineMutation', response.body);
                        ui(false, 'Successful added: ' + response.body.id);
                        if (success !== undefined) success();
                    },

                    error => {
                        ui(false, error.bodyText);
                    }
                );
            } else throw 'Not setted ui callback';
        },

        removeAirlineAction({ commit, state}, { id, ui }) {
            if (ui !== undefined) {
                ui(true, null);

                commit('removeAirlineMutation', id);
                apiAirlines.deleteAirline(id).then(
                    response => {
                        ui(false, response.bodyText);
                    },

                    error => {
                        ui(false, error.bodyText)
                    }
                );
            } else throw 'Not setted ui callback';
        }
    }
}