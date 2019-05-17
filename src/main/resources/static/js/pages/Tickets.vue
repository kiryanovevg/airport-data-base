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

                <app-dropdown :title="'Passenger'"
                              :loading="loading.passengers"
                              :items="passengers"
                              v-model="input.passenger"
                              :fill="getPassengerText"
                />

                <div class="input-group mb-3"
                     v-if="!loading.passengers"
                >
                    <div class="input-group-prepend">
                        <span class="input-group-text">Luggage</span>
                    </div>
                    <input type="number" min="1" class="form-control" aria-label="Luggage" aria-describedby="inputGroup-sizing-sm"
                           v-model.number="input.luggage"
                    >
                </div>

                <app-dropdown :title="'Flight'"
                              :loading="loading.flights"
                              :items="flights"
                              :fill="getFlightText"
                              v-model="input.flight"
                />

                <div class="mb-3" v-if="visibilityPlaces">
                    <app-dropdown :title="'Place'"
                                  :loading="loading.places"
                                  :items="places"
                                  v-model="input.place"
                                  :fill="item => item"
                    />
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
                        {{ getTicketText(ticket) }}
                    </li>
                </ul>

                <hr>

            </div>

            <div class="col" v-if="selected.ticket">
                <div class="d-flex justify-content-between">
                    <h2>Билет #{{ selected.ticket.id }}</h2>
                    <button
                            type="button"
                            class="btn btn-danger ml-4"
                            v-on:click="deleteTicket"
                    >Delete</button>
                </div>

                <div>Passenger: {{ selectedPassenger }}</div>
                <div>Flight: #{{ selected.ticket.flight.id }}</div>
                <div>Place: {{ selected.ticket.place }}</div>
                <div>Price: {{ selected.ticket.flight.price }}</div>
                <div>Luggage: {{ selected.ticket.luggage }}</div>
                <div>Direction: {{
                    selected.ticket.flight.direction.fromCity.name
                    + ' => '
                    + selected.ticket.flight.direction.toCity.name
                    }}</div>
                <div>Airplane: {{ selected.ticket.flight.airplane.model }}</div>
                <div>Schedule: {{ selectedSchedule }}</div>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import {getScheduleText, parseSchedule} from "../util/store.js";

    export default {
        name: "Tickets",
        data() {
            return {
                input: {
                    flight: null,
                    place: null,
                    passenger: null,
                    luggage: null,
                },
                selected: {
                    ticket: null,
                },
                loading: {
                    add: null,
                    flights: null,
                    tickets: null,
                    places: null,
                    passengers: null,
                },
                message: null,
            }
        },
        created() {
            this.loadFlights();
            this.loadTickets();
            this.loadPassengers();
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
                passengers: state => state.passengers.data,
            }),

            inputFlight() {
                return this.input.flight
            },

            visibilityPlaces() {
                return this.inputFlight != null
            },

            selectedSchedule() {
                return getScheduleText(parseSchedule(this.selected.ticket.flight.schedule))
            },

            selectedPassenger() {
                return this.getPassengerText(this.selected.ticket.passenger);
            }
        },
        methods: {
            ...mapActions({
                getPassengers: 'passengers/getAction',
                getFlights: 'flights/getAction',
                getTickets: 'tickets/getAction',
                getPlaces: 'places/getAction',
                createTicket: 'tickets/addAction',
                removeTicket: 'tickets/removeAction',
            }),

            getPassengerText(passenger) {
                return passenger.surname + ' '
                    + passenger.name + ' '
                    + passenger.patronymic + ' | '
                    + passenger.series + ' '
                    + passenger.number
            },

            getTicketText(ticket) {
                return '#' + ticket.flight.id + ' | ' + this.getFlightText(ticket.flight) + ' | Place: ' + ticket.place
            },

            getFlightText(flight) {
                const direction = flight.direction;
                const fromCity = direction.fromCity;
                const toCity = direction.toCity;
                return fromCity.name + ' => ' + toCity.name + ' | Price: ' + flight.price;
            },

            selectTicket: function(index) {
                this.selected.ticket = this.tickets[index];
            },

            clearAddField() {
                this.input.passenger = null;
                this.input.luggage = null;
                this.input.flight = null;
                this.input.place = null;
            },

            addTicket() {
                const self = this;

                if (!self.input.flight
                    || !self.input.place
                    || !self.input.luggage) {
                    self.message = "Заполните все поля!";
                    return;
                }

                const data = {
                    flight: self.input.flight.id,
                    luggage: self.input.luggage,
                    place: self.input.place,
                    passenger: self.input.passenger.id
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

            deleteTicket() {
                const self = this;
                const selected = self.selected.ticket;

                self.selected.ticket = null;
                self.removeTicket({
                    id: selected.id,
                    ui(loading, msg) {
                        self.loading.tickets = loading;
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
                        self.loading.tickets = loading;
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

            loadPassengers() {
                const self = this;
                self.getPassengers({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.passengers = loading;
                    }
                });
            },
        }
    }
</script>

<style scoped>

</style>