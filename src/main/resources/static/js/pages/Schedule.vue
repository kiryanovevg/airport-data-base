<template>
    <div class="container pt-5 pb-5">
        <div class="row">
            <div class="col">
                <h1>Schedule</h1>
            </div>
        </div>

        <hr>

        <app-message :message="message"/>

        <div class="row">
            <div class="col">

                <div class="row">
                    <div class="col col-md-10">
                        <div class="d-flex justify-content-between">
                            <div class="form-group mr-1">
                                <input type="number" min="1" class="form-control" id="dayInput" placeholder="Day" v-model.number="input.date.day">
                            </div>

                            <div class="form-group mr-1">
                                <input type="number" min="1" class="form-control" id="monthInput" placeholder="Month" v-model.number="input.date.month">
                            </div>

                            <div class="form-group mr-1">
                                <input type="number" min="1" class="form-control" id="yearInput" placeholder="Year" v-model.number="input.date.year">
                            </div>

                            <div class="form-group mr-1">
                                <input type="number" min="1" class="form-control" id="hourInput" placeholder="Hour" v-model.number="input.date.hour">
                            </div>

                            <div class="form-group mr-1">
                                <input type="number" min="1" class="form-control" id="minuteInput" placeholder="Minute" v-model.number="input.date.minute">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-auto">
                        <button type="button" class="btn btn-outline-primary" v-on:click="setCurrentDate">Текущее время</button>
                        <button type="button" class="btn btn-outline-primary" v-on:click="setCurrentDatePlusDay">Текущее время + день</button>
                    </div>
                </div>

                <div class="row d-flex justify-content-center" v-if="!loading.add">
                    <div class="col">
                        <div class="d-inline-flex p-2 bd-highlight justify-content-center">
                            {{ selected.departureDate }}
                        </div>
                    </div>
                    <div class="col-md-auto">
                        <div class="btn-group" role="group" aria-label="Basic example">
                            <button type="button" class="btn btn-outline-primary" v-on:click="setDepartureDate">Дата вылета</button>
                            <button type="button" class="btn btn-primary" v-on:click="addSchedule">Add</button>
                            <button type="button" class="btn btn-outline-primary" v-on:click="setArrivalDate">Дата прибытия</button>
                        </div>
                    </div>
                    <div class="col">
                        <div class="d-inline-flex p-2 bd-highlight justify-content-center">
                            {{ selected.arrivalDate }}
                        </div>
                    </div>
                </div>

                <div class="d-flex justify-content-center">
                    <div class="spinner-border" role="status"
                         v-if="loading.add"
                    >
                        <span class="sr-only">Loading...</span>
                    </div>
                </div>

                <hr>

                <div v-if="selected.schedule">
                    <div class="d-flex justify-content-between">
                        <div class="d-inline-flex p-2 bd-highlight">{{ selected.schedule }}</div>
                        <button
                                type="button"
                                class="btn btn-danger"
                                v-on:click="deleteSchedule"
                        >X</button>
                    </div>

                    <hr>
                </div>

                <app-list :items="schedules"
                          v-model="selected.schedule"
                          :fill="getScheduleText"
                />

            </div>
        </div>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import {parseSchedule, getScheduleText} from "../util/store.js";

    export default {
        name: "Schedule",
        data() {
            return {
                input: {
                    date: {
                        day: null,
                        month: null,
                        year: null,
                        hour: null,
                        minute: null
                    }
                },
                selected: {
                    arrivalDate: null,
                    departureDate: null,
                    schedule: null
                },
                loading: {
                    add: false
                },
                message: null,
            }
        },
        computed: {
            ...mapState({
                schedules: (state) => state.schedule.data
            })
        },
        created() {
            this.loadSchedules();
        },
        methods: {
            ...mapActions({
                getSchedule: 'schedule/getAction',
                createSchedule: 'schedule/addAction',
                removeSchedule: 'schedule/removeAction',
            }),

            getScheduleText(schedule) {
                return getScheduleText(schedule);
            },

            selectSchedule: function (index) {
                this.selected.schedule = this.schedules[index];
            },

            setCurrentDate: function () {
                const date = new Date();
                this.input.date.day = date.getDate();
                this.input.date.month = date.getMonth() + 1;
                this.input.date.year = date.getFullYear();
                this.input.date.hour = date.getHours();
                this.input.date.minute = date.getMinutes();
            },

            setCurrentDatePlusDay: function () {
                const date = new Date();
                this.input.date.day = date.getDate() + 1;
                this.input.date.month = date.getMonth() + 1;
                this.input.date.year = date.getFullYear();
                this.input.date.hour = date.getHours();
                this.input.date.minute = date.getMinutes();
            },

            clearInputFields: function () {
                this.input.date.day = null;
                this.input.date.month = null;
                this.input.date.year = null;
                this.input.date.hour = null;
                this.input.date.minute = null;
            },

            checkInputFields: function () {
                return this.input.date.year
                    && this.input.date.month
                    && this.input.date.day
                    && this.input.date.hour
                    && this.input.date.minute
            },

            setDate(setter) {
                if (this.checkInputFields()) {
                    const date = new Date(
                        this.input.date.year,
                        this.input.date.month - 1,
                        this.input.date.day,
                        this.input.date.hour,
                        this.input.date.minute
                    );

                    setter(date);

                    this.clearInputFields();
                    this.message = null;
                } else {
                    this.message = "Заполните все поля!"
                }
            },

            setDepartureDate: function () {
                this.setDate(date => this.selected.departureDate = date);
            },

            setArrivalDate: function () {
                this.setDate(date => this.selected.arrivalDate = date);
            },

            loadSchedules: function () {
                const self = this;

                self.getSchedule({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.add = loading;
                    },
                    transformData(data) {
                        return data.map((value, index, array) => {
                            return parseSchedule(value);
                        })
                    },
                });
            },

            addSchedule: function () {
                const self = this;

                if (!self.selected.departureDate
                    || !self.selected.arrivalDate) {
                    self.message = "Выберите даты!";
                    return
                }

                self.selected.schedule = null;
                self.createSchedule({
                    data: {
                        departure: self.selected.departureDate,
                        arrival: self.selected.arrivalDate
                    },
                    transformData(data) {
                        return parseSchedule(data);
                        // return data;
                    },
                    ui(loading, msg) {
                        self.message = msg;
                        self.loading.add = loading;
                    },
                    complete() {
                        self.selected.departureDate = null;
                        self.selected.arrivalDate = null;
                    }
                });
            },

            deleteSchedule: function () {
                const self = this;
                const selected = self.selected.schedule;

                self.selected.schedule = null;
                self.removeSchedule({
                    id: selected.id,
                    ui(loading, msg) {
                        self.loading.add = loading;
                        self.message = msg;
                    }
                });
            },
        }
    }
</script>

<style scoped>

</style>