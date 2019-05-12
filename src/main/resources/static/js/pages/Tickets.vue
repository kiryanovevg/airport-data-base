<template>
    <div class="container pt-4 pb-4">
        <div class="row">
            <div class="col">
                <h1>Tickets</h1>
            </div>
        </div>

        <hr>

        <div class="row">
            <div class="col-md-5">

                <app-message :message="message"/>

                <div class="mb-3">
                    <app-loading :loading="loading.flights"/>
                    <div class="input-group"
                         v-if="!loading.flights"
                    >
                        <div class="input-group-prepend">
                            <label class="input-group-text" for="flightInput">Flight</label>
                        </div>
                        <select class="custom-select" id="flightInput"
                                v-model="input.flight"
                        >
                            <option
                                    v-for="(flight, index) in flights"
                                    :value="flight"
                            >{{ flight }}</option>
                        </select>
                        <!--                            <span>Выбрано: {{ input.fromCity }}</span>-->
                    </div>
                </div>

                <div class="mb-3" v-if="visibilityPlaces">
                    <app-loading :loading="loading.places"/>
                    <div class="input-group"
                         v-if="!loading.places"
                    >
                        <div class="input-group-prepend">
                            <label class="input-group-text" for="placeInput">Place</label>
                        </div>
                        <select class="custom-select" id="placeInput"
                                v-model="input.place"
                        >
                            <option
                                    v-for="(place, index) in places"
                                    :value="place"
                            >{{ place }}</option>
                        </select>
                        <!--                            <span>Выбрано: {{ input.fromCity }}</span>-->
                    </div>
                </div>

                <app-loading :loading="loading.add"/>
                <div class="btn-group d-flex justify-content-end"
                     role="group"
                     aria-label="Basic example"
                     v-if="!loading.add"
                >
                    <button type="button" class="btn btn-secondary" v-on:click="clearAddField">Clear</button>
                    <button type="button" class="btn btn-primary" v-on:click="addTicket">Add</button>
                </div>

                <hr>

                <app-loading :loading="loading.tickets"/>
                <ul v-if="!loading.tickets" class="list-group">
                    <li
                            class="list-group-item list-group-item-action"
                            v-for="(ticket, index) in tickets"
                            v-on:click="selectTicket(index)"
                            v-bind:class="{ active: ticket === selected.ticket }"
                    >
                        {{ ticket }}
                    </li>
                </ul>

                <hr>

            </div>

            <div class="col" v-if="">
                <!--<div class="d-flex justify-content-between">
                    <h2>{{ selected.flight.id }}</h2>
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
                <div>Schedule: {{ selectedSchedule }}</div>-->
            </div>
        </div>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";

    export default {
        name: "Tickets",
        data() {
            return {
                input: {
                    flight: null,
                    place: null,
                },
                selected: {
                    ticket: null,
                },
                loading: {
                    add: null,
                    flights: null,
                    tickets: null,
                    places: null,
                },
                message: null,
            }
        },
        created() {
            this.loadFlights();
            this.loadTickets();
        },
        watch: {
            inputFlight(newValue, oldValue) {
                if (newValue) this.loadPlaces(newValue.id);
            }
        },
        computed: {
            ...mapState({
                flights: state => state.flights.data,
                tickets: state => state.tickets.data,
                places: state => state.places.data,
            }),

            inputFlight() {
                return this.input.flight
            },

            visibilityPlaces() {
                return this.inputFlight != null
            }
        },
        methods: {
            ...mapActions({
                getFlights: 'flights/getAction',
                getTickets: 'tickets/getAction',
                getPlaces: 'places/getAction',
                createTicket: 'tickets/addAction',
            }),

            selectTicket: function(index) {
                this.selected.ticket = this.tickets[index];
            },

            clearAddField() {
                this.input.flight = null;
                this.input.place = null;
            },

            addTicket() {
                const self = this;

                if (!self.input.flight
                    || !self.input.place) {
                    self.message = "Заполните все поля!";
                    return;
                }

                const data = {
                    flight: self.input.flight.id,
                    luggage: true,
                    place: self.input.place
                };

                self.clearAddField();
                self.createTicket({
                    data,
                    ui(loading, msg) {
                        self.loading.add = loading;
                        self.message = msg;
                    },
                    complete() {
                        self.loadFlights();
                    },
                });
            },

            loadFlights() {
                const self = this;
                self.getFlights({
                    params: {
                        free_places: "true"
                    },
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.flights = loading;
                    }
                });
            },

            loadTickets() {
                const self = this;
                self.getTickets({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.flights = loading;
                    }
                });
            },

            loadPlaces(flightId) {
                const self = this;
                self.getPlaces({
                    params: {
                        id: flightId
                    },
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.places = loading;
                    }
                });
            },
        }
    }
</script>

<style scoped>

</style>