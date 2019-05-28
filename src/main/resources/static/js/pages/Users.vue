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
                            <span class="input-group-text">Credentials</span>
                        </div>
                        <input type="text" class="form-control"
                               aria-label="Username"
                               placeholder="Username"
                               aria-describedby="inputGroup-sizing-sm"
                               v-model="input.username"
                        >
                        <input type="text" class="form-control"
                               aria-label="Password"
                               placeholder="Password"
                               aria-describedby="inputGroup-sizing-sm"
                               v-model="input.password"
                        >
                    </div>
                </div>

                <div class="mb-3">
                    <app-loading :loading="loading.roles"/>
                    <div class="input-group input-group-sm"
                         v-if="!loading.roles"
                    >
                        <div class="input-group-prepend">
                            <label class="input-group-text" for="roleInput">Role</label>
                        </div>
                        <select class="custom-select" id="roleInput"
                                v-model="input.role"
                        >
                            <option
                                    v-for="(role, index) in roles"
                                    :value="role"
                            >{{ role }}</option>
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
                    <button type="button" class="btn btn-primary" v-on:click="addUser">Add</button>
                </div>

                <app-list :loading="loading.users"
                          :items="users"
                          v-model="selected.user"
                          :fill="item => item.login"
                />

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
                <div>Role: {{ selected.user.userRole }}</div>
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
                    role: null,
                },
                selected: {
                    user: null,
                },
                loading: {
                    add: null,
                    users: null,
                    roles: null,
                },
                message: null,
            }
        },
        created() {
            this.loadUsers();
            this.loadRoles();
        },
        computed: {
            ...mapState({
                users: state => state.users.data,
                roles: state => state.roles.data,
            })
        },
        methods: {
            ...mapActions({
                getUsers: 'users/getAction',
                getRoles: 'roles/getAction',
                createUser: 'users/addAction',
                removeUser: 'users/removeAction',
            }),

            selectUser(index) {
                this.selected.user = this.users[index];
            },

            clearAddField: function () {
                this.input.username = null;
                this.input.password = null;
                this.input.role = null;
            },

            addUser() {
                const self = this;

                if (!self.input.username
                    || !self.input.password
                    || !self.input.role) {
                    self.message = "Заполните все поля!";
                    return;
                }

                const data = {
                    login: self.input.username,
                    password: self.input.password,
                    userRole: self.input.role
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

            loadRoles() {
                const self = this;
                self.getRoles({
                    ui(loading, msg) {
                        if (msg != null) self.message = msg;
                        self.loading.roles = loading;
                    }
                });
            },
        }
    }
</script>

<style scoped>

</style>