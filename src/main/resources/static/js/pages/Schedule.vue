<template>
    <div class="container pt-5 pb-5">
        <!--<div class="row">
            <div class="col">
                <h1>Schedule</h1>
            </div>
        </div>
        <hr>-->

        <app-message :message="message"/>

        <div class="row">
            <div class="col">

                <div class="row d-flex justify-content-center align-items-center" v-if="!loading.add">
                    <div class="col">
                        <app-date-time-picker
                                :label="'Дата вылета'"
                                v-model="input.departureDate"
                        />
                    </div>
                    <div class="col">
                        <app-date-time-picker
                                v-model="input.arrivalDate"
                                :label="'Дата прибытия'"
                        />
                    </div>
                    <div class="col-md-auto">
                            <button type="button" class="btn btn-primary" v-on:click="addSchedule">Add</button>
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
                        <div class="col">
                            <div>ID: {{ selected.schedule.id }}</div>
                            <div>Время вылета: {{ selectedDepartureDate }}</div>
                            <div>Время прибытия: {{ selectedArrivalDate }}</div>
                        </div>
                        <div class="d-flex p-2 bd-highlight">

                        </div>
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
    import {parseSchedule, getScheduleText, scheduleDateFormat} from "../util/store.js";
    import moment from "moment";

    export default {
        name: "Schedule",
        data() {
            return {
                input: {
                    departureDate: null,
                    arrivalDate: null,
                },
                selected: {
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
            }),

            selectedDepartureDate() {
                return moment(this.selected.schedule.departure)
                    .format(scheduleDateFormat)
            },

            selectedArrivalDate() {
                return moment(this.selected.schedule.arrival)
                    .format(scheduleDateFormat)
            }
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

                if (!self.input.departureDate
                    || !self.input.arrivalDate) {
                    self.message = "Выберите даты!";
                    return
                }

                const body = {
                    departure: self.input.departureDate,
                    arrival: self.input.arrivalDate
                };

                self.selected.schedule = null;
                self.createSchedule({
                    data: body,
                    transformData(data) {
                        return parseSchedule(data);
                        // return data;
                    },
                    ui(loading, msg) {
                        self.message = msg;
                        self.loading.add = loading;
                    },
                    complete() {
                        self.input.departureDate = null;
                        self.input.arrivalDate = null;
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