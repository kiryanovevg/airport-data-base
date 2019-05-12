<template>
    <div class="container pt-4 pb-4">
        <div class="row">
            <div class="col">
                <h1>Airplanes</h1>
            </div>
        </div>

        <hr>

        <div class="row">
            <div class="col-md-4">
                <app-message :message="message"/>

                <div class="spinner-border" role="status"
                     v-if="loading.content"
                >
                    <span class="sr-only">Loading...</span>
                </div>

                <div id="addAirplane" v-if="!loading.add">
                    <p>Add airplane</p>

                    <div class="input-group input-group-sm mb-3">
                        <div class="input-group-prepend">
                            <span class="input-group-text">Model</span>
                        </div>
                        <input type="text" class="form-control" aria-label="Model" aria-describedby="inputGroup-sizing-sm"
                               v-model="input.model"
                        >
                    </div>

                    <div class="input-group input-group-sm mb-3">
                        <div class="input-group-prepend">
                            <span class="input-group-text">Capacity</span>
                        </div>
                        <input type="number" min="1" class="form-control" aria-label="Capacity" aria-describedby="inputGroup-sizing-sm"
                               v-model.number="input.capacity"
                        >
                    </div>

                    <div class="input-group mb-3">
                        <div class="input-group-prepend">
                            <label class="input-group-text" for="inputGroupAirline">Airline</label>
                        </div>
                        <select class="custom-select" id="inputGroupAirline"
                                v-model="selected.airline"
                        >
                            <option
                                    v-for="(airline, index) in airlines"
                                    v-bind:value="airline"
                            >{{ airline.name }}</option>
                        </select>
                    </div>
                    <span>Выбрано: {{ selected.airline }}</span>

                    <div class="d-flex justify-content-end">
                        <button type="button" class="btn btn-secondary mr-1" v-on:click="clearAddField">Clear</button>
                        <button type="button" class="btn btn-primary" v-on:click="addAirplane">Add</button>
                    </div>
                </div>

                <hr>

                <ul class="list-group"
                >
                    <li
                            class="list-group-item list-group-item-action"
                            v-for="(airplane, index) in airplanes"
                            v-on:click="selectAirplane(index)"
                            v-bind:class="{ active: airplane === selected.airplane }"
                    >
                        {{index + 1}} - {{ airplane.model }}
                    </li>
                </ul>

                <hr>

            </div>

            <div class="col" v-if="selected.airplane">
                <div class="d-flex justify-content-between">
                    <h2>{{ selected.airplane.model }}</h2>
                    <button
                            type="button"
                            class="btn btn-danger ml-4"
                            v-on:click="deleteAirplane"
                    >Delete</button>
                </div>

                <div>Capacity: {{ selected.airplane.capacity }}</div>
                <div>Airline: {{ getAirlineFromId(selected.airplane.airlineId).name }}</div>
            </div>



        </div>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";

    export default {
        name: "Airplanes",
        data() {
            return {
                input: {
                    model: null,
                    capacity: null,
                },
                selected: {
                    airline: null,
                    airplane: null,
                },
                loading: {
                    add: false,
                    content: false
                },
                message: null
            }
        },
        computed: {
            ...mapState({
                airlines: state => state.airlines.data,
                airplanes: state => state.airplanes.data
            }),
        },
        created() {
            this.loadAirplanes();
            this.loadAirlines();
        },
        methods: {
            ...mapActions({
                getAirlines: 'airlines/getAction',
                getAirplanes: 'airplanes/getAction',
                createAirplane: 'airplanes/addAction',
                removeAirplane: 'airplanes/removeAction',
            }),

            select(s) {
              alert(s)
            },

            clearAddField: function () {
                this.input.model = null;
                this.input.capacity = null;
                this.selected.airline = null;
            },

            selectAirplane: function(index) {
                this.selected.airplane = this.airplanes[index]
            },

            getAirlineFromId: function (id) {
                let result = null;

                if (id) {
                    this.airlines.forEach(function (value) {
                        if (value.id === id) {
                            result = value;
                        }
                    });
                }

                return result;
            },

            loadAirlines: function() {
                const self = this;
                self.getAirlines({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.add = loading;
                    }
                });
            },

            loadAirplanes: function () {
                const self = this;
                self.getAirplanes({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.content = loading;
                    }
                });
            },

            addAirplane: function () {
                const self = this;

                if (!self.input.model
                    || !self.input.capacity
                    || !self.selected.airline) {
                    self.message = "Заполните все поля!";
                    return;
                }

                self.createAirplane({
                    data: {
                        model: self.input.model,
                        capacity: self.input.capacity,
                        airlineId: self.selected.airline.id
                    },
                    ui(loading, msg) {
                        self.loading.add = loading;
                        self.message = msg;
                    },
                    complete() {
                        self.input.model = null;
                        self.input.capacity = null;
                        self.selected.airline = null;
                        self.loadAirlines()
                    }
                });
            },

            deleteAirplane: function() {
                const self = this;
                const selected = self.selected.airplane;

                self.selected.airplane = null;
                self.removeAirplane({
                    id: selected.id,
                    ui(loading, msg) {
                        self.loading.content = loading;
                        self.message = msg;
                    }
                });
            },
        }
    }
</script>

<style scoped>

</style>