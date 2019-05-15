<template>
    <div class="container pt-4 pb-4">
        <div class="row">
            <div class="col">
                <h1>Users</h1>
            </div>
        </div>

        <hr>

        <div class="row">
            <div class="col-md-5">

                <app-message :message="message"/>

                <div class="mb-3">
                    <div class="input-group input-group-sm mb-3">
                        <div class="input-group-prepend">
                            <span class="input-group-text">Username</span>
                        </div>
                        <input type="text" class="form-control" aria-label="Username" aria-describedby="inputGroup-sizing-sm"
                               v-model="input.username"
                        >
                    </div>
                </div>

                <div class="mb-3">
                    <div class="input-group input-group-sm mb-3">
                        <div class="input-group-prepend">
                            <span class="input-group-text">Password</span>
                        </div>
                        <input type="text" class="form-control" aria-label="Password" aria-describedby="inputGroup-sizing-sm"
                               v-model="input.password"
                        >
                    </div>
                </div>

                <app-loading :loading="loading.add"/>
                <div class="btn-group d-flex justify-content-end"
                     role="group"
                     aria-label="Basic example"
                     v-if="!loading.add"
                >
                    <button type="button" class="btn btn-secondary" v-on:click="clearAddField">Clear</button>
                    <button type="button" class="btn btn-primary" v-on:click="addUser">Add</button>
                </div>

                <hr>

                <app-loading :loading="loading.users"/>
                <ul v-if="!loading.users" class="list-group">
                    <li
                            class="list-group-item list-group-item-action"
                            v-for="(user, index) in users"
                            v-on:click="selectUser(index)"
                            v-bind:class="{ active: user === selected.user}"
                    >
                        {{ user.login }}
                    </li>
                </ul>

                <hr>

            </div>

            <div class="col" v-if="selected.user">
                <div class="d-flex justify-content-between">
                    <h2>{{ selected.user.login}}</h2>
                    <button
                            type="button"
                            class="btn btn-danger ml-4"
                            v-on:click="deleteUser"
                    >Delete</button>
                </div>

                <div>Password: {{ selected.user.password }}</div>
                <div>Admin permission: {{ selected.user.adminPermission }}</div>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";

    export default {
        name: "Users",
        data() {
            return {
                input: {
                    username: null,
                    password: null,
                },
                selected: {
                    user: null,
                },
                loading: {
                    add: null,
                    users: null,
                },
                message: null,
            }
        },
        created() {
            this.loadUsers();
        },
        computed: {
            ...mapState({
                users: state => state.users.data,
            })
        },
        methods: {
            ...mapActions({
                getUsers: 'users/getAction',
                createUser: 'users/addAction',
                removeUser: 'users/removeAction',
            }),

            selectUser(index) {
                this.selected.user = this.users[index];
            },

            clearAddField: function () {
                this.input.username = null;
                this.input.password = null;
            },

            addUser() {
                const self = this;

                if (!self.input.username
                    || !self.input.password) {
                    self.message = "Заполните все поля!";
                    return;
                }

                const data = {
                    login: self.input.username,
                    password: self.input.password
                };

                self.input.username = null;
                self.input.password = null;

                self.createUser({
                    data,
                    ui(loading, msg) {
                        self.loading.add = loading;
                        self.message = msg;
                    }
                });
            },

            deleteUser() {
                const self = this;
                const selected = self.selected.user;

                self.selected.user = null;
                self.removeUser({
                    id: selected.id,
                    ui(loading, msg) {
                        self.message = msg;
                        self.loading.users = loading;
                    }
                });
            },

            loadUsers() {
                const self = this;
                self.getUsers({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.users = loading;
                    }
                });
            },
        }
    }
</script>

<style scoped>

</style>