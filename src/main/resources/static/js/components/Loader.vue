<template>
    <div class="spinner-border" role="status"
         v-if="loading"
    >
        <span class="sr-only">Loading...</span>
    </div>
</template>

<script>
    export default {
        name: "Loader",
        props: ['loading']
    }
</script>

<style scoped>

</style>