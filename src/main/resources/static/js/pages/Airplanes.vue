<template>
    <div class="container pt-5">
        <div class="row">
            <div class="col">
                <h1>Airplanes</h1>
            </div>
        </div>

        <hr>

        <div class="row">
            <div class="col-md-4">
                <div
                        class="alert alert-secondary"
                        role="alert"
                        v-if="message"
                >
                    Message: {{ message }}
                </div>

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
                        <input type="text" class="form-control" aria-label="Capacity" aria-describedby="inputGroup-sizing-sm"
                               v-model="input.capacity"
                        >
                    </div>

                    <div class="input-group mb-3">
                        <div class="input-group-prepend">
                            <label class="input-group-text" for="inputGroupAirline">Airline</label>
                        </div>
                        <select class="custom-select" id="inputGroupAirline" v-model="selected.airlineName">
                            <option
                                    v-for="(airline, index) in airlines"
                            >{{ airline.name }}</option>
                        </select>
                    </div>
                    <span>Выбрано: {{ getAirlineFromName(selected.airlineName) }}</span>

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
    export default {
        name: "Airplanes",
        data() {
            return {
                input: {
                    model: null,
                    capacity: null,
                },
                selected: {
                    airlineName: null,
                    airplane: null
                },
                loading: {
                    add: false,
                    content: false
                },
                message: null,
                airlines: [],
                airplanes: []
            }
        },
        created() {
            this.loadAirplanes();
            this.loadAirlines();
        },
        methods: {
            clearAddField: function () {
                this.input.model = null;
                this.input.capacity = null;
                this.input.selectedAirlineName = null;
            },

            selectAirplane: function(index) {
                this.input.selectedAirplane = this.airplanes[index]
            },

            addAirplane: function () {
                var self = this;

                if (self.input.model
                    && self.input.capacity
                    && self.input.selectedAirlineName) {

                    var airline = self.getAirlineFromName(
                        self.input.selectedAirlineName
                    );

                    if (airline) {
                        var body = JSON.stringify({
                            model: self.input.model,
                            capacity: self.input.capacity,
                            airlineId: airline.id
                        });

                        self.input.model = null;
                        self.input.capacity = null;
                        self.input.selectedAirlineName = null;

                        self.message = null;
                        self.loading.content = true;
                        // self.loading.add = true;

                        $.ajax({
                            url: "/airplanes",
                            type: "POST",
                            contentType:"application/json",
                            data: body,
                            success: function(response) {
                                // self.loadAirlines();
                                self.loading.content = false;
                                self.airplanes.push(response);
                                self.message = 'Successful added';
                            },
                            error: function(xhr) {
                                self.loading.content = false;
                                // self.loading.add = false;
                                self.message = xhr.responseText;
                            }
                        });
                    } else {
                        self.message = "Произошла ошибка при выборе!"
                    }
                } else {
                    self.message = "Заполните все поля!"
                }
            },

            loadAirplanes: function () {
                var self = this;
                self.loading.content = true;

                $.ajax({
                    url: "/airplanes",
                    type: "GET",
                    success: function(response) {
                        self.loading.content = false;
                        self.airplanes = response;
                    },
                    error: function(xhr) {
                        self.loading.content = false;
                        self.message = xhr.responseText;
                    }
                });
            },

            deleteAirplane: function() {
                var self = this;
                var selected = self.input.selectedAirplane;

                self.loading.content = true;
                self.input.selectedAirplane = null;
                self.message = null;

                var index = self.airplanes.indexOf(selected);
                if (index > -1) {
                    self.airplanes.splice(index, 1);
                }

                $.ajax({
                    url: "/airplanes/" + selected.id,
                    type: "DELETE",
                    success: function(response) {
                        self.loading.content = false;
                        self.message = "Successful removed!"
                    },
                    error: function(xhr) {
                        self.loading.content = false;
                        self.message = xhr.responseText;
                    }
                });
            },

            loadAirlines: function() {
                var self = this;
                self.loading.add = true;

                $.ajax({
                    url: "/airlines",
                    type: "GET",
                    success: function(response) {
                        self.loading.add = false;
                        self.airlines = response;
                    },
                    error: function(xhr) {
                        self.loading.add = false;
                        self.message = xhr.responseText;
                    }
                });
            },

            getAirlineFromName: function (name) {
                var val = null;

                if (name) {
                    this.airlines.forEach(function (value) {
                        if (value.name === name) {
                            val = value;
                        }
                    });
                }

                return val;
            },

            getAirlineFromId: function (id) {
                var result = null;

                if (id) {
                    this.airlines.forEach(function (value) {
                        if (value.id === id) {
                            result = value;
                        }
                    });
                }

                return result;
            }
        }
    }
</script>

<style scoped>

</style>