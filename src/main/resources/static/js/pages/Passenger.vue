<template>
    <div class="container pt-4 pb-4">
        <!--<div class="row">
            <div class="col">
                <h1>Passenger</h1>
            </div>
        </div>
        <hr>-->

        <div class="row">
            <div class="col-md-5">

                <app-message :message="message"/>

                <div class="mb-2">
                    <div class="input-group input-group-sm mb-2">
                        <div class="input-group-prepend">
                            <span class="input-group-text">Full name</span>
                        </div>
                        <input type="text" class="form-control"
                               aria-label="Surname"
                               placeholder="Surname"
                               aria-describedby="inputGroup-sizing-sm"
                               v-model="input.surname"
                        >
                        <input type="text" class="form-control"
                               aria-label="Name"
                               placeholder="Name"
                               aria-describedby="inputGroup-sizing-sm"
                               v-model="input.name"
                        >
                        <input type="text" class="form-control"
                               aria-label="Patronymic"
                               placeholder="Patronymic"
                               aria-describedby="inputGroup-sizing-sm"
                               v-model="input.patronymic"
                        >
                    </div>
                </div>

                <div class="input-group input-group-sm mb-2">
                    <div class="input-group-prepend">
                        <span class="input-group-text">Passport</span>
                    </div>
                    <input type="number" min="1" class="form-control"
                           aria-label="Series"
                           placeholder="Series"
                           aria-describedby="inputGroup-sizing-sm"
                           v-model.number="input.series"
                    >
                    <input type="number" min="1" class="form-control"
                           aria-label="Number"
                           placeholder="Number"
                           aria-describedby="inputGroup-sizing-sm"
                           v-model.number="input.number"
                    >
                </div>

                <!--<div class="input-group input-group-sm mb-2">
                    <div class="input-group-prepend">
                        <span class="input-group-text">Number</span>
                    </div>
                    <input type="number" min="1" class="form-control" aria-label="Number" aria-describedby="inputGroup-sizing-sm"
                           v-model.number="input.number"
                    >
                </div>-->

                <app-loading :loading="loading.add"/>
                <div class="btn-group d-flex justify-content-end"
                     role="group"
                     aria-label="Basic example"
                     v-if="!loading.add"
                >
                    <button type="button" class="btn btn-secondary" v-on:click="clearAddField">Clear</button>
                    <button type="button" class="btn btn-primary" v-on:click="addPassenger">Add</button>
                </div>

                <app-list :loading="loading.passengers"
                          :items="passengers"
                          v-model="selected.passenger"
                          :fill="item"
                />

            </div>

            <div class="col" v-if="selected.passenger">
                <div class="d-flex justify-content-between">
                    <h2>{{ passengerName(selected.passenger) }}</h2>
                    <button
                            type="button"
                            class="btn btn-danger ml-4"
                            v-on:click="deletePassenger"
                    >Delete</button>
                </div>

                <div>Отчество: {{ selected.passenger.patronymic }}</div>
                <div>Паспорт: {{ passengerPassport(selected.passenger) }}</div>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";

    export default {
        name: "Passenger",
        data() {
            return {
                input: {
                    surname: null,
                    name: null,
                    patronymic: null,
                    series: null,
                    number: null,
                },
                selected: {
                    passenger: null,
                },
                loading: {
                    add: null,
                    passengers: null,
                },
                message: null,
            }
        },
        created() {
            this.loadPassenger();
        },
        computed: {
            ...mapState({
                passengers: (state) => state.passengers.data
            }),
        },
        methods: {
            ...mapActions({
                getPassengers: 'passengers/getAction',
                createPassenger: 'passengers/addAction',
                removePassengers: 'passengers/removeAction',
            }),

            item(passenger) {
                return this.passengerName(passenger) + ' | ' + this.passengerPassport(passenger)
            },

            passengerName(passenger) {
                return passenger.surname + ' ' + passenger.name;
            },

            passengerPassport(passenger) {
                return passenger.series + ' ' + passenger.number;
            },

            selectPassenger(index) {
                this.selected.passenger = this.passengers[index];
            },

            clearAddField: function () {
                this.input.surname = null;
                this.input.name = null;
                this.input.patronymic = null;
                this.input.series = null;
                this.input.number = null;
            },

            loadPassenger() {
                const self = this;

                self.getPassengers({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.add = loading;
                    },
                });
            },

            addPassenger() {
                const self = this;

                if (!self.input.surname
                    || !self.input.name
                    || !self.input.patronymic
                    || !self.input.series
                    || !self.input.number) {
                    self.message = "Заполните все поля!";
                    return
                }

                const data = {
                    surname: self.input.surname,
                    name: self.input.name,
                    patronymic: self.input.patronymic,
                    series: self.input.series,
                    number: self.input.number,
                };

                self.input.surname = null;
                self.input.name = null;
                self.input.patronymic = null;
                self.input.series = null;
                self.input.number = null;

                self.selected.passenger = null;
                self.createPassenger({
                    data,
                    ui(loading, msg) {
                        self.message = msg;
                        self.loading.add = loading;
                    },
                });
            },

            deletePassenger() {
                const self = this;
                const selected = self.selected.passenger;

                self.selected.passenger = null;
                self.removePassengers({
                    id: selected.id,
                    ui(loading, msg) {
                        self.loading.passengers = loading;
                        self.message = msg;
                    },
                });
            }
        },
    }
</script>

<style scoped>

</style>