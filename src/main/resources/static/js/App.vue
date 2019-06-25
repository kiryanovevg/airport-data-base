<template>
    <div>
        <div v-if="profile">
            User: {{ profile.login }} - {{ profile.role }}
            <button @click="logout">
                Logout
            </button>
            <button
                    v-if="profile && $route.path !== '/'"
                    @click="navigateToMainPage">
                MainPage
            </button>
        </div>
<!--        <app-nav-bar/>-->

        <router-view
                @authenticate="authenticate"
                :routes="routes"
        ></router-view>
    </div>
</template>

<script>
    import NavBar from "./components/NavBar.vue";

    import Airlines from "./pages/Airlines.vue";
    import Airplanes from "./pages/Airplanes.vue";
    import Directions from "./pages/Directions.vue";
    import Schedule from "./pages/Schedule.vue";
    import Flights from "./pages/Flights.vue";
    import Tickets from "./pages/Tickets.vue";
    import Users from "./pages/Users.vue";
    import Passenger from "./pages/Passenger.vue";

    export default {
        data() {
            return {
                profile: null,
                routes: null,
            }
        },
        components: {
            'app-nav-bar': NavBar,
        },
        watch: {
            $route(newRoute, oldRoute) {
                // console.log(newRoute.path + ' -> ' + oldRoute.path);
                // this.handlePath();
            }
        },
        created() {
            this.loadProfile();
            this.handlePath();
            this.handleRole();
        },
        methods: {
            handlePath() {
                if (!this.profile) this.redirectToLogin();
                else if (this.$route.path === '/login') this.redirectToMainPage();
            },
            handleRole() {
                if (!this.profile) return;

                if (this.profile.role === 'Admin') {
                    this.routes = [
                        {path: '/airlines', component: Airlines},
                        {path: '/airplanes', component: Airplanes, children: [ { path: '/airplanes/:id', component: Airplanes } ]},
                        {path: '/directions', component: Directions},
                        {path: '/schedule', component: Schedule},
                        {path: '/flights', component: Flights},
                        {path: '/tickets', component: Tickets},
                        {path: '/users', component: Users},
                        {path: '/passenger', component: Passenger},
                    ];
                }
                if (this.profile.role === 'Dispatcher') {
                    this.routes = [
                        {path: '/airplanes', component: Airplanes, children: [ { path: '/airplanes/:id', component: Airplanes } ]},
                        {path: '/directions', component: Directions},
                        {path: '/schedule', component: Schedule},
                        {path: '/flights', component: Flights},
                    ];
                }
                if (this.profile.role === 'Cashier') {
                    this.routes = [
                        {path: '/tickets', component: Tickets},
                        {path: '/passenger', component: Passenger},
                    ];
                }
                if (this.profile.role === 'Security') {
                    this.routes = [
                        {path: '/tickets', component: Tickets},
                    ];
                }

                // this.$router.addRoutes(this.routes)
            },
            loadProfile() {
                if (localStorage.profile !== undefined) {
                    const json = JSON.parse(localStorage.profile);
                    if (json.login && json.role) this.profile = json
                }
            },
            saveProfile(profile) {
                const data = {
                    login: profile.login,
                    role: profile.userRole
                };

                this.profile = data;
                localStorage.profile = JSON.stringify(data);
            },
            clearProfile() {
                this.profile = null;
                localStorage.removeItem('profile');
            },

            authenticate(profile) {
                this.saveProfile(profile);
                this.redirectToMainPage();
                this.handleRole();
            },
            logout() {
                this.clearProfile();
                this.redirectToLogin();
            },
            redirectToLogin() {
                this.$router.replace('/login');
            },
            redirectToMainPage() {
                this.$router.replace('/');
            },
            navigateToMainPage() {
                this.$router.push('/');
            },
        }
    }
</script>

<style scoped>
    /*.fixed {
        position: fixed !important;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        !*background: red;*!
    }*/
</style>