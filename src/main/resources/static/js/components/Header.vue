<template>
    <div class="header d-flex justify-content-between flex" v-if="profile">
        <!--<button
                v-if="profile && $route.path !== '/'"
                @click="$emit('navigateToMainPage')">
            Back to MainPage
        </button>-->

        <div class="user d-flex flex-column">
            <div class="login">{{ profile.login }}</div>
            <div class="role">{{ profile.role }}</div>
        </div>

        <button @click="$emit('logout')">LOGOUT</button>
    </div>
</template>

<script>
    export default {
        props: ['profile'],
        name: "NavBar"
    }
</script>

<style scoped>
    * {
        padding: 0;
    }

    .header {
        width: 100vw;
        background: white;
        padding: 8px 16px;
        box-shadow: 0 1px 5px 1px rgba(0,0,0,0.5);
        display: flex;
        justify-content: space-between;
    }

    .login {
        font-weight: bold;
    }

    .user {
        text-align: center;
    }

    .role {
        color: #383838;
    }

    button {
        margin: 6px;
        border-radius: 3px;
        border: none;
        background-color: #212121;
        color: white;
        font-weight: bold;
        padding: 5px 32px;
        font-size: 14px;
        transition: background-color;
        transition-duration: 0.2s;
    }

</style>