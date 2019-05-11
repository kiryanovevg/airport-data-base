<template>
    <div
            class="alert alert-secondary"
            role="alert"
            v-if="message"
    >
        Message: {{ message }}
    </div>
</template>

<script>
    export default {
        props: ['message'],
        data() {
            return {

            }
        },
    }
</script>

<style scoped>

</style>