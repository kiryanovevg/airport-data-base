<template>
    <div class="main-container">
        <app-header
                :profile="profile"
                :title="title"
                @logout="logout"
                @navigateToMainPage="navigateToMainPage"
        />
        <div class="content">
            <app-nav-bar
                    :profile="profile"
                    v-model="routes"
            />
            <router-view
                    class="router"
                    @authenticate="authenticate"
                    :routes="routes"
            ></router-view>
        </div>
    </div>
</template>

<script>
    import Header from "./components/Header.vue";
    import NavBar from "./components/NavBar.vue";

    export default {
        data() {
            return {
                profile: null,
                routes: null,
                title: null,
            }
        },
        components: {
            'app-header': Header,
            'app-nav-bar': NavBar,
        },
        watch: {
            '$route'(newRoute, oldRoute) {
                // console.log(newRoute.path + ' -> ' + oldRoute.path);
                // this.handlePath();
                this.title = null
            }
        },
        created() {
            this.loadProfile();
            this.handlePath();
            // this.handleRole();
        },
        methods: {
            handlePath() {
                if (!this.profile) this.redirectToLogin();
                else if (this.$route.path === '/login') this.redirectToMainPage();
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
                // this.handleRole();
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
    * {
        padding: 0;
        overflow: hidden;
    }

    .main-container {
        /*background: #0062cc;*/
        display: flex;
        flex-direction: column;
        height: 100vh;
        font-family: "Cantarell", sans-serif;
    }

    .content {
        display: flex;
        height: 100vh;
    }

    .router {
        overflow: auto;
        flex-grow: 1;
        /*background-color: red;*/
    }

</style>