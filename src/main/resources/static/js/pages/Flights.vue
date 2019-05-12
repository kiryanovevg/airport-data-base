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
                    <div class="mb-3">
                        <app-loading :loading="loading.airplanes"/>
                        <div class="input-group"
                             v-if="!loading.airplanes"
                        >
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="airplaneInput">Airplane</label>
                            </div>
                            <select class="custom-select" id="airplaneInput"
                                    v-model="input.airplane"
                            >
                                <option
                                        v-for="(airplane, index) in airplanes"
                                        :value="airplane"
                                >{{ airplane }}</option>
                            </select>
                            <!--                            <span>Выбрано: {{ input.fromCity }}</span>-->
                        </div>
                    </div>

                    <div class="mb-3">
                        <app-loading :loading="loadingDirections"/>
                        <div class="input-group"
                             v-if="!loadingDirections"
                        >
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="directionInput">Direction</label>
                            </div>
                            <select class="custom-select" id="directionInput"
                                    v-model="input.direction"
                            >
                                <option
                                        v-for="(direction, index) in directions"
                                        :value="direction"
                                >{{ direction }}</option>
                            </select>
                            <!--                            <span>Выбрано: {{ input.fromCity }}</span>-->
                        </div>
                    </div>

                    <div class="mb-3">
                        <app-loading :loading="loading.schedules"/>
                        <div class="input-group mb-3"
                             v-if="!loading.schedules"
                        >
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="scheduleInput">Schedule</label>
                            </div>
                            <select class="custom-select" id="scheduleInput"
                                    v-model="input.schedule"
                            >
                                <option
                                        v-for="(schedule, index) in schedules"
                                        :value="schedule"
                                >{{ schedule }}</option>
                            </select>
                            <!--                            <span>Выбрано: {{ input.fromCity }}</span>-->
                        </div>
                    </div>

                    <div class="btn-group d-flex justify-content-end"
                         role="group"
                         aria-label="Basic example"
                         v-if="!loadingData"
                    >
                        <button type="button" class="btn btn-secondary" v-on:click="clearAddField">Clear</button>
                        <button type="button" class="btn btn-primary" v-on:click="addFlight">Add</button>
                    </div>
                </div>

                <hr>

                <app-loading :loading="loading.flights"/>
                <ul v-if="!loading.flights" class="list-group">
                    <li
                            class="list-group-item list-group-item-action"
                            v-for="(flight, index) in flights"
                            v-on:click="selectFlight(index)"
                            v-bind:class="{ active: flight === selected.flight }"
                    >
                        {{index + 1}} - {{ flight }}
                    </li>
                </ul>

                <hr>

            </div>

            <div class="col" v-if="selected.flight">
                <div class="d-flex justify-content-between">
                    <h2>{{ selected.flight.id }}</h2>
                    <button
                            type="button"
                            class="btn btn-danger ml-4"
                            v-on:click="deleteFlight"
                    >Delete</button>
                </div>

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
    import {parseSchedule} from "../util/store.js";

    export default {
        name: "Flights",
        data() {
            return {
                input: {
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
                return this.getById(this.airplanes, this.selected.flight.airplane).model
            },

            selectedDirection() {
                const direction = this.getById(this.directions, this.selected.flight.direction);
                const fromCity = this.getById(this.cities, direction.fromCityId);
                const toCity = this.getById(this.cities, direction.toCityId);
                return fromCity.name + ' => ' + toCity.name
            },

            selectedSchedule() {
                const schedule = this.getById(this.schedules, this.selected.flight.schedule);
                const dep = schedule.departure;
                const arr = schedule.arrival;

                const depStr = `${dep.getDate() + 1}-${dep.getMonth()}-${dep.getFullYear()} ${dep.getHours()}:${dep.getMinutes()}`;
                const arrStr = `${arr.getDate() + 1}-${arr.getMonth()}-${arr.getFullYear()} ${arr.getHours()}:${arr.getMinutes()}`;

                return depStr + ' => ' + arrStr
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

            selectFlight: function(index) {
                this.selected.flight = this.flights[index];
            },

            clearAddField: function () {
                this.input.airplane = null;
                this.input.direction = null;
                this.input.schedule = null;
            },

            addFlight() {
                const self = this;

                if (!self.input.airplane
                    || !self.input.direction
                    || !self.input.schedule) {
                    self.message = "Заполните все поля!";
                    return;
                }

                self.createFlight({
                    data: {
                        airplane: self.input.airplane.id,
                        direction: self.input.direction.id,
                        schedule: self.input.schedule.id,
                    },
                    ui(loading, msg) {
                        self.loading.add = loading;
                        self.message = msg;
                    },
                    complete() {
                        self.input.airplane = null;
                        self.input.direction = null;
                        self.input.schedule = null;
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

            getById(data, id) {
                let result = null;
                data.forEach(item => {
                    if (item.id === id) result = item;
                });
                return result;
            },
        }
    }
</script>

<style scoped>

</style>