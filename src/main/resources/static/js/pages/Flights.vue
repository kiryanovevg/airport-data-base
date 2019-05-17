<template>
    <div class="container pt-4 pb-4">
        <div class="row">
            <div class="col">
                <h1>Flights</h1>
            </div>
        </div>

        <hr>

        <div class="row">
            <div class="col-md-5">

                <app-message :message="message"/>

                <app-loading :loading="loading.add"/>
                <div v-if="!loading.add">
                    <div class="input-group mb-3">
                        <div class="input-group-prepend">
                            <span class="input-group-text">Price</span>
                        </div>
                        <input type="number" min="1" class="form-control" aria-label="Price" aria-describedby="inputGroup-sizing-sm"
                               v-model.number="input.price"
                        >
                    </div>

                    <app-dropdown :title="'Airplane'"
                                  :loading="loading.airplanes"
                                  :items="airplanes"
                                  v-model="input.airplane"
                                  :fill="item => item.model"
                    />

                    <app-dropdown :title="'Direction'"
                                  :loading="loadingDirections"
                                  :items="directions"
                                  v-model="input.direction"
                                  :fill="getDirectionText"
                    />

                    <app-dropdown :title="'Schedule'"
                                  :loading="loading.schedules"
                                  :items="notUsedSchedules"
                                  v-model="input.schedule"
                                  :fill="getScheduleText"
                    />

                    <div class="btn-group d-flex justify-content-end"
                         role="group"
                         aria-label="Basic example"
                         v-if="!loadingData"
                    >
                        <button type="button" class="btn btn-secondary" v-on:click="clearAddField">Clear</button>
                        <button type="button" class="btn btn-primary" v-on:click="addFlight">Add</button>
                    </div>
                </div>

                <app-list :loading="loading.flights"
                          :items="flights"
                          v-model="selected.flight"
                          :fill="listItem"
                />

            </div>

            <div class="col" v-if="selected.flight">
                <div class="d-flex justify-content-between">
                    <h2>Рейс #{{ selected.flight.id }}</h2>
                    <button
                            type="button"
                            class="btn btn-danger ml-4"
                            v-on:click="deleteFlight"
                    >Delete</button>
                </div>

                <div>Price: {{ selectedPrice }}</div>
                <br>
                <div>Airplane: {{ selectedAirplane }}</div>
                <br>
                <div>Direction: {{ selectedDirection }}</div>
                <br>
                <div>Schedule: {{ selectedSchedule }}</div>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import {parseSchedule, getById, getScheduleText} from "../util/store.js";

    export default {
        name: "Flights",
        data() {
            return {
                input: {
                    price: null,
                    airplane: null,
                    direction: null,
                    schedule: null,
                },
                selected: {
                    flight: null,
                },
                loading: {
                    airplanes: null,
                    directions: null,
                    cities: null,
                    schedules: null,
                    flights: null,
                    add: null,
                },
                message: null,
            }
        },
        computed: {
            ...mapState({
                airplanes: state => state.airplanes.data,
                cities: state => state.cities.data,
                directions: state => state.directions.data,
                schedules: state => state.schedule.data,
                flights: state => state.flights.data,
            }),

            loadingDirections() {
                return this.loading.directions && this.loading.cities
            },

            loadingData() {
                return this.loading.airplanes
                    && this.loading.schedules
                    && this.loadingDirections
            },

            selectedAirplane() {
                return getById(this.airplanes, this.selected.flight.airplane.id).model
            },

            selectedDirection() {
                const direction = this.selected.flight.direction;
                return this.getDirectionText(direction);
            },

            selectedSchedule() {
                const schedule = getById(this.schedules, this.selected.flight.schedule.id);
                return getScheduleText(schedule);
            },

            selectedPrice() {
                return this.selected.flight.price
            },

            notUsedSchedules() {
                const result = [];
                this.schedules.forEach(item => {
                    if (item.flight == null) result.push(item);
                });
                return result;
            }
        },
        created() {
            this.loadAirplanes();
            this.loadCities();
            this.loadDirections();
            this.loadSchedules();
            this.loadFlights();
        },
        methods: {
            ...mapActions({
                getAirplanes: 'airplanes/getAction',
                getCities: 'cities/getAction',
                getDirections: 'directions/getAction',
                getSchedule: 'schedule/getAction',

                getFlights: 'flights/getAction',
                createFlight: 'flights/addAction',
                removeFlight: 'flights/removeAction',
            }),

            getScheduleText(schedule) {
                return getScheduleText(schedule);
            },

            getDirectionText(direction) {
                return direction.fromCity.name + ' => ' + direction.toCity.name
            },

            listItem(flight) {
                return this.getDirectionText(flight.direction) + ' | Price: ' + flight.price;
            },

            selectFlight: function(index) {
                this.selected.flight = this.flights[index];
            },

            clearAddField: function () {
                this.input.price = null;
                this.input.airplane = null;
                this.input.direction = null;
                this.input.schedule = null;
            },

            addFlight() {
                const self = this;

                if (!self.input.price
                    || !self.input.airplane
                    || !self.input.direction
                    || !self.input.schedule) {
                    self.message = "Заполните все поля!";
                    return;
                }

                self.createFlight({
                    data: {
                        price: self.input.price,
                        airplane: self.input.airplane.id,
                        direction: self.input.direction.id,
                        schedule: self.input.schedule.id,
                    },
                    ui(loading, msg) {
                        self.loading.add = loading;
                        self.message = msg;
                    },
                    complete() {
                        self.selected.flight = null;
                        self.input.price = null;
                        self.input.airplane = null;
                        self.input.direction = null;
                        self.input.schedule = null;
                        self.loadSchedules();
                    }
                });
            },

            deleteFlight() {
                const self = this;
                const selected = self.selected.flight;

                self.selected.flight = null;
                self.removeFlight({
                    id: selected.id,
                    ui(loading, msg) {
                        self.message = msg;
                        self.loading.flights = loading;
                    },
                    complete() {
                        self.loadSchedules();
                    }
                });
            },

            loadFlights() {
                const self = this;
                self.getFlights({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.flights = loading;
                    }
                });
            },

            loadAirplanes: function () {
                const self = this;
                self.getAirplanes({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.airplanes = loading;
                    }
                });
            },

            loadCities: function () {
                const self = this;
                self.getCities({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.cities = loading;
                    }
                });
            },

            loadDirections: function () {
                const self = this;
                self.getDirections({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.directions = loading;
                    }
                });
            },

            loadSchedules: function () {
                const self = this;

                self.getSchedule({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.schedules = loading;
                    },
                    transformData(data) {
                        return data.map((value, index, array) => {
                            return parseSchedule(value);
                        })
                    },
                });
            },


        }
    }
</script>

<style scoped>

</style>