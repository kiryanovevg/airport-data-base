<template>
    <nav
            v-if="profile"
    >
        <app-head-title :title="selected.component.name"/>

        <div class="item-container">
            <router-link
                    v-for="(route, index) in routes"
                    :to="route.path"
                    :item="route"
                    :key="route.path"
            >
                <div
                        class="nav-item"
                        v-on:click="selected = route"
                        v-bind:class="{ active: route === selected }"
                >

                    {{ route.component.name }}
                </div>
            </router-link>
        </div>

<!--        {{ selected }}-->
    </nav>
</template>

<script>
    import Airlines from "../pages/Airlines.vue";
    import Airplanes from "../pages/Airplanes.vue";
    import Directions from "../pages/Directions.vue";
    import Schedule from "../pages/Schedule.vue";
    import Flights from "../pages/Flights.vue";
    import Tickets from "../pages/Tickets.vue";
    import Users from "../pages/Users.vue";
    import Passenger from "../pages/Passenger.vue";

    const routes = {
        airlines: {path: '/airlines', component: Airlines},
        airplanes: {path: '/airplanes', component: Airplanes, children: [ { path: '/airplanes/:id', component: Airplanes } ]},
        directions: {path: '/directions', component: Directions},
        schedule: {path: '/schedule', component: Schedule},
        flights: {path: '/flights', component: Flights},
        tickets: {path: '/tickets', component: Tickets},
        passenger: {path: '/passenger', component: Passenger},
        users: {path: '/users', component: Users},
    };

    export default {
        props: ['profile'],
        name: "NavBar",
        data() {
            return {
                selected: null,
            }
        },
        watch: {
            '$route'(newRoute, oldRoute) {
                const checkGlobalRoutes = function (route) {
                    let contains = false;
                    Object.keys(routes).forEach((key) => {
                        const condition = route.path.includes(routes[key].path);
                        if (!contains && condition) {
                            contains = true;
                        }
                    });
                    return contains;
                };

                if (checkGlobalRoutes(newRoute)) {
                    const to = this.containsRoute(newRoute);
                    if (!to) {//not allowed
                        if (this.routes.length > 0) this.selected = this.routes[0];
                        if (this.selected) this.routeTo(this.selected);
                    } else {//allowed
                        this.selected = to
                    }
                } else {//route isn't from this global routes
                    if (newRoute.path === '/') {
                        if (this.selected) this.routeTo(this.selected);
                        else {
                            if (this.routes.length > 0) this.selected = this.routes[0];
                            if (this.selected) this.routeTo(this.selected);
                        }
                    }
                }
            }
        },
        created() {
            this.selected = this.containsRoute(this.$router.currentRoute);
            if (!this.selected) {
                if (this.routes.length > 0) this.selected = this.routes[0];
                if (this.selected) this.routeTo(this.selected);
            }
        },
        computed: {
            routes() {
                if (!this.profile) {
                    return [];
                }

                if (this.profile.role === 'Admin') {
                    return [
                        routes.airlines,
                        routes.airplanes,
                        routes.directions,
                        routes.schedule,
                        routes.flights,
                        routes.tickets,
                        routes.users,
                        routes.passenger,
                    ];
                }
                if (this.profile.role === 'Dispatcher') {
                    return [
                        routes.airplanes,
                        routes.directions,
                        routes.schedule,
                        routes.flights,
                    ];
                }
                if (this.profile.role === 'Cashier') {
                    return [
                        routes.tickets,
                        routes.passenger,
                    ];
                }
                if (this.profile.role === 'Security') {
                    return [
                        routes.tickets,
                    ];
                }

                return null;
            }
        },
        methods: {
            routeTo(route) {
                this.$router.replace(route.path)
            },

            containsRoute(route) {
                let contains = null;
                this.routes.forEach((item) => {
                    const condition = route.path.includes(item.path);
                    if (!contains && condition) {
                        contains = item;
                    }
                });
                return contains;
            },
        }
    }
</script>

<style scoped>
    nav {
        display: flex;
        flex-direction: column;
        /*background-color: #5E35B1;*/
        background: #0062cc;
        flex-shrink: 0;
        /*justify-content: space-between;*/
        border-bottom-right-radius: 4px;
        border-top-right-radius: 4px;
        min-width: 160px;
        max-width: 320px;
    }

    .nav-item {
        color: white;
        padding: 12px 16px;
        transition: background-color;
        transition-duration: 0.2s;
        cursor: pointer;
    }

    .nav-item:hover {
        background: #006ee4;
        /*background-color: #693bc5;*/
    }

    .active {
        background: #006ee4;
        /*background-color: #693bc5;*/
    }

    button:hover {
        background-color: #383838;
    }
</style>