<template>
    <div>
        <button
                v-if="profile"
                @click="logout">
            1231
        </button>
        <router-view
                @authenticate="authenticate"
        ></router-view>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                msg: 'MESSAGE',
                profile: null
            }
        },
        created() {
            this.loadProfile();
            if (!this.profile) this.redirectToLogin()
        },
        methods: {
            loadProfile() {
                if (typeof localStorage.profile !== 'undefined') {
                    const json = JSON.parse(localStorage.profile);
                    if (json.login) this.profile = json
                }
            },
            saveProfile(login) {
                const profile = {
                    login: login
                };

                this.profile = profile;
                localStorage.profile = JSON.stringify(profile);
            },
            clearProfile() {
                this.profile = null;
                localStorage.removeItem('profile');
            },

            redirectToLogin() {
                this.$router.replace('login');
            },
            authenticate(login) {
                this.saveProfile(login);
                this.$router.replace('/');
            },
            logout() {
                this.clearProfile();
                this.redirectToLogin();
            }
        }
    }
</script>

<style scoped>

</style>