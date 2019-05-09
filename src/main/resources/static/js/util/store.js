import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        airlines: Store(Vue.resource('/api/airlines{/id}')),
        airplanes: Store(Vue.resource('/api/airplanes{/id}')),
        directions: Store(Vue.resource('/api/directions{/id}')),
        cities: Store(Vue.resource('/api/directions/cities{/id}')),
    }
})

function Store(api) {
    return {
        namespaced: true,
        state: {
            data: []
        },

        getter: {

        },

        mutations: {
            setMutation(state, data) {
                state.data = data;
            },

            addMutation(state, data) {
                state.data.push(data)
            },

            removeMutation(state, id) {
                let data;

                state.data.forEach(item => {
                    if (item.id === id) data = item;
                });

                if (data !== null) {
                    const index = state.data.indexOf(data);
                    if (index > -1) state.data.splice(index, 1);
                } else throw 'Airline not found with id: ' + id;
            }
        },

        actions: {
            getAction({ commit, state }, ui) {
                if (ui !== undefined) {
                    ui(true, null);

                    api.get().then(
                        response => {
                            commit('setMutation', response.body);
                            ui(false, null);
                        },

                        error => {
                            ui(false, error.bodyText)
                        }
                    )
                } else throw 'Not setted ui callback';
            },

            addAction({ commit, state }, { data, ui, complete }) {
                if (ui !== undefined) {
                    ui(true, null);

                    api.save({}, data).then(
                        response => {
                            commit('addMutation', response.body);
                            ui(false, 'Successful added: ' + response.body.id);
                            if (complete !== undefined) complete();
                        },

                        error => {
                            ui(false, error.bodyText);
                        }
                    );
                } else throw 'Not setted ui callback';
            },

            removeAction({ commit, state}, { id, ui, complete }) {
                if (ui !== undefined) {
                    ui(true, null);

                    commit('removeMutation', id);
                    api.delete({id: id}).then(
                        response => {
                            ui(false, response.bodyText);
                            if (complete !== undefined) complete();
                        },

                        error => {
                            ui(false, error.bodyText)
                        }
                    );
                } else throw 'Not setted ui callback';
            }
        }
    }
}