<template>
    <div class="container pt-4 pb-4">
        <!--<div class="row">
            <div class="col">
                <h1>Direction</h1>
            </div>
        </div>
        <hr>-->

        <div class="row">
            <div class="col-md-4">

                <app-message :message="message"/>

                <div id="addCity" v-if="!loading.add">
                    <div class="d-flex justify-content-between">
                        <div class="d-inline-flex p-2 bd-highlight">
                            <h4>Cities</h4>
                        </div>

                        <div class="spinner-border" role="status"
                             v-if="loading.city"
                        >
                            <span class="sr-only">Loading...</span>
                        </div>
                    </div>


                    <div class="input-group mb-3">
                        <input type="text" class="form-control" placeholder="City name" aria-label="Airline name" aria-describedby="basic-addon2"
                               v-model="input.cityName"
                        >
                        <div class="input-group-append">
                            <button class="btn btn-outline-secondary" type="button" v-on:click="addCity">Add</button>
                        </div>
                    </div>
                </div>

                <div v-if="selected.city">
                    <hr>
                    <div class="d-flex justify-content-between">
                        <div class="d-inline-flex p-2 bd-highlight">{{selected.city.name}}</div>
                        <button
                                type="button"
                                class="btn btn-danger"
                                v-on:click="deleteCity"
                        >X</button>
                    </div>
                </div>

                <app-list :items="cities"
                          v-model="selected.city"
                          :fill="item => item.name"
                />

                <!--<hr>
                <ul class="list-group">
                    <li
                            class="list-group-item list-group-item-action"
                            v-for="(city, index) in cities"
                            v-on:click="selectCity(index)"
                            v-bind:class="{ active: city === selected.city }"
                    >
                        {{index + 1}} - {{ city.name}}
                    </li>
                </ul>

                <hr>-->

            </div>

            <div class="col">
                <div v-if="!loading.city">
                    <div class="d-flex justify-content-between">
                        <div class="d-inline-flex p-2 bd-highlight">
                            <h4>Directions</h4>
                        </div>

                        <div class="spinner-border" role="status"
                             v-if="loading.direction"
                        >
                            <span class="sr-only">Loading...</span>
                        </div>
                    </div>

                    <div>
                        <app-dropdown :title="'From'"
                                      :items="cities"
                                      v-model="input.fromCity"
                                      :fill="item => item.name"
                        />

                        <app-dropdown :title="'to'"
                                      :items="cities"
                                      v-model="input.toCity"
                                      :fill="item => item.name"
                        />

                    </div>

                    <div class="d-flex justify-content-end">
                        <button type="button" class="btn btn-outline-primary btn-sm" v-on:click="addDirection">Add Direction</button>
                    </div>

                    <hr>
                </div>

                <div v-if="selected.direction">
                    <div class="d-flex justify-content-between">
                        <div class="d-inline-flex p-2 bd-highlight">
                            {{ selected.direction.fromCity.name }} =>
                            {{ selected.direction.toCity.name }}
                        </div>
                        <button
                                type="button"
                                class="btn btn-danger"
                                v-on:click="deleteDirection"
                        >X</button>
                    </div>

                    <div class="d-inline-flex p-2 bd-highlight">{{ selected.direction }}</div>

                    <hr>
                </div>

                <app-list :items="directions"
                          v-model="selected.direction"
                          :fill="item => `${item.fromCity.name} => ${item.toCity.name}`"
                />
            </div>



        </div>
    </div>
</template>

<script>
    import {mapActions, mapMutations, mapState} from "vuex";

    export default {
        name: "Directions",
        data() {
            return {
                input: {
                    cityName: null,
                    fromCity: null,
                    toCity: null
                },
                selected: {
                    city: null,
                    direction: null,
                },
                loading: {
                    city: false,
                    direction: false
                },
                message: null,
            }
        },
        computed: {
            ...mapState({
                cities: state => state.cities.data,
                directions: state => state.directions.data,
            }),
        },
        created() {
            this.loadCities();
            this.loadDirections();
        },
        methods: {
            ...mapActions({
                getCities: 'cities/getAction',
                createCity: 'cities/addAction',
                removeCity: 'cities/removeAction',
                getDirections: 'directions/getAction',
                createDirection: 'directions/addAction',
                removeDirection: 'directions/removeAction',
            }),
            ...mapMutations({
                setDirections: 'directions/setMutation'
            }),

            selectDirection: function (index) {
                this.selected.direction = this.directions[index];
            },

            loadCities: function () {
                const self = this;
                self.getCities({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.city = loading;
                    }
                });
            },

            loadDirections: function () {
                const self = this;
                self.getDirections({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.direction = loading;
                    }
                });
            },

            addCity: function () {
                const self = this;

                if (!self.input.cityName) {
                    self.message = "Заполните все поля!";
                    return;
                }

                self.selected.city = null;
                self.createCity({
                    data: {
                        name: self.input.cityName
                    },
                    ui(loading, msg) {
                        self.message = msg;
                        self.loading.city = loading;
                    },
                    complete() {
                        self.input.cityName = null;
                    }
                });
            },

            deleteCity: function () {
                const self = this;
                const selected = self.selected.city;

                self.selected.direction = null;
                self.selected.city = null;
                self.setDirections(null);
                self.removeCity({
                    id: selected.id,
                    ui(loading, msg) {
                        self.message = msg;
                        self.loading.city = loading;
                    },
                    complete() {
                        self.loadDirections();
                    }
                });
            },

            addDirection: function () {
                const self = this;

                if (!self.input.fromCity
                    || !self.input.toCity) {
                    self.message = "Заполните все поля!";
                    return;
                }

                const data = {
                    fromCityId: self.input.fromCity.id,
                    toCityId: self.input.toCity.id
                };

                self.input.selectedDirection = null;
                self.input.fromCity = null;
                self.input.toCity = null;
                self.createDirection({
                    data,
                    ui(loading, msg) {
                        self.message = msg;
                        self.loading.direction = loading;
                    }
                });
            },

            deleteDirection: function () {
                const self = this;
                const selected = self.selected.direction;

                self.selected.direction = null;
                self.removeDirection({
                    id: selected.id,
                    ui(loading, msg) {
                        self.loading.direction = loading;
                        self.message = msg;
                    }
                });
            },
        }
    }
</script>

<style scoped>

</style>