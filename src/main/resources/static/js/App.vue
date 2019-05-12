<template>
    <div>
        <button
                v-if="profile"
                @click="logout">
            Logout
        </button>
        <button
                v-if="profile && $route.path !== '/'"
                @click="navigateToMainPage">
            MainPage
        </button>

<!--        <app-nav-bar/>-->

        <router-view
                @authenticate="authenticate"
        ></router-view>
    </div>
</template>

<script>
    import NavBar from "./components/NavBar.vue";

    export default {
        data() {
            return {
                msg: 'MESSAGE',
                profile: null,
            }
        },
        components: {
            'app-nav-bar': NavBar,
        },
        watch: {
            $route(newRoute, oldRoute) {
                // console.log(newRoute.path + ' -> ' + oldRoute.path);
                this.handlePath();
            }
        },
        created() {
            this.loadProfile();
            this.handlePath();
        },
        methods: {
            handlePath() {
                if (!this.profile) this.redirectToLogin();
                else if (this.$route.path === '/login') this.redirectToMainPage();
            },
            loadProfile() {
                if (localStorage.profile !== undefined) {
                    const json = JSON.parse(localStorage.profile);
                    if (json.login) this.profile = json
                }
            },
            saveProfile(login) {
                const profile = {
                    login: login
                };

                this.profile = profile;
                // localStorage.profile = JSON.stringify(profile);
            },
            clearProfile() {
                this.profile = null;
                localStorage.removeItem('profile');
            },

            authenticate(login) {
                this.saveProfile(login);
                this.$router.replace('/');
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

</style>