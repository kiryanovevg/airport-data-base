<template>
    <div class="container pt-5 pb-5">
        <div class="row">
            <div class="col">
                <h1>Airlines</h1>
            </div>
        </div>

        <hr>

        <div
                class="alert alert-secondary"
                role="alert"
                v-if="message"
        >
            Message: {{ message }}
        </div>

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

                <hr>

                <div class="spinner-border" role="status"
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

                <hr>

            </div>
            <div class="col">
                <div class="d-flex justify-content-between" v-if="selected">
                    <h2>{{ selected.name }}</h2>
                    <button
                            type="button"
                            class="btn btn-danger ml-4"
                            v-on:click="deleteAirline(selected)"
                    >Delete</button>
                </div>

                <div>{{ selected }}</div>
            </div>
        </div>
    </div>
</template>

<script>
    import apiAirlines from "api/airlines.js";

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
                airlines: []
            }
        },
        created() {
            this.loadAirlines()
        },
        methods: {
            selectAirline(index) {
                this.selected = this.airlines[index]
            },

            deleteAirline: function(selected) {
                const self = this;

                self.loading = true;
                self.selected = null;
                self.message = null;

                const index = self.airlines.indexOf(selected);
                if (index > -1) {
                    self.airlines.splice(index, 1);
                }

                apiAirlines.deleteAirline(selected.id).then(
                    response => {
                        self.loading = false;
                        self.message = 'Successful deleted';
                    },

                    error => {
                        self.loading = false;
                        self.message = error.bodyText;
                    }
                );
            },

            addAirline: function () {
                const self = this;

                if (!self.input.airlineName) {
                    return
                }

                self.selected = null;
                self.message = null;
                self.loading = true;

                apiAirlines.addAirline(self.input.airlineName).then(
                    response => {
                        self.loading = false;
                        self.input.airlineName = null;

                        self.airlines.push(response.body);
                        self.message = 'Successful added: ' + response.body.id;
                    },

                    error => {
                        self.loading = false;
                        self.message = error.bodyText;
                    }
                );
            },

            loadAirlines: function() {
                const self = this;
                self.loading = true;

                apiAirlines.loadAirlines().then(
                    success => {
                        self.loading = false;
                        self.airlines = success.body;
                    },

                    error => {
                        alert(error);
                        self.loading = false;
                        self.message = error.bodyText;
                    }
                );
            }
        }
    }
</script>

<style scoped>

</style>