<template>
    <div class="container pt-4 pb-4">
        <!--<div class="row">
            <div class="col">
                <h1>Airlines</h1>
            </div>
        </div>
        <hr>-->

        <app-message :message="message"/>

        <div class="row">
            <div class="col-md-4">
                <div class="input-group mb-3">
                    <input type="text" class="form-control" placeholder="Airline name" aria-label="Airline name" aria-describedby="basic-addon2"
                           v-model="input.airlineName"
                    >
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="button" v-on:click="addAirline">Add</button>
                    </div>
                </div>

                <app-list :loading="loading"
                          :items="airlines"
                          v-model="selected"
                          :fill="item => item.name"
                />

                <!--<hr>

                <div class="spinner-border" userRole="status"
                     v-if="loading"
                >
                    <span class="sr-only">Loading...</span>
                </div>

                <ul class="list-group"
                >
                    <li
                            class="list-group-item list-group-item-action"
                            v-for="(airline, index) in airlines"
                            v-on:click="selectAirline(index)"
                            v-bind:class="{ active: airline === selected }"
                    >
                        {{index + 1}} - {{ airline.name }}
                    </li>
                </ul>

                <hr>-->

            </div>
            <div class="col" v-if="selected">
                <div class="d-flex justify-content-between">
                    <h2>{{ selected.name }}</h2>
                    <button
                            type="button"
                            class="btn btn-danger ml-4"
                            v-on:click="deleteAirline(selected)"
                    >Delete</button>
                </div>

                <div v-if="selected.airplanes.length !== 0">
                    <div>Airplanes:</div>
                    <div
                            v-for="(airplane, index) in selected.airplanes"
                    >
                        <router-link :to="{ path: `/airplanes/${airplane.id}` }">
                            {{ airplane.model }}
                        </router-link>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";

    export default {
        name: "Airlines",
        data() {
            return {
                input: {
                    airlineName: ''
                },
                loading: false,
                message: null,
                selected: null,
            }
        },
        computed: {
            ...mapState({
                airlines: state => state.airlines.data
            })
        },
        created() {
            this.loadAirlines();
        },
        methods: {
            ...mapActions({
                get: 'airlines/getAction',
                add: 'airlines/addAction',
                remove: 'airlines/removeAction',
            }),

            loadAirlines() {
                const self = this;
                self.get({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading = loading;
                    }
                })
            },
            deleteAirline(selected) {
                const self = this;

                self.selected = null;
                self.remove({
                    id: selected.id,
                    ui(loading, msg) {
                        self.loading = loading;
                        self.message = msg;
                    }
                });
            },

            addAirline() {
                const self = this;

                if (!self.input.airlineName) {
                    return
                }

                self.selected = null;
                self.add({
                    data: {
                        name: self.input.airlineName
                    },
                    ui(loading, msg) {
                        self.message = msg;
                        self.loading = loading;
                    },
                    complete() {
                        self.input.airlineName = null;
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>