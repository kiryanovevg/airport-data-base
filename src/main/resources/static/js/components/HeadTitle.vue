<template>
    
</template>

<script>
    export default {
        name: 'HeadTitle',
        props: ['title'],
        data() {
            return {
                prefix: "Airport"
            }
        },
        watch: {
            title: {
                immediate: true,
                handler() {
                    this.setTitle(this.prefix + ' - ' + this.title);
                }
            }
        },
        beforeDestroy() {
            this.setTitle(this.prefix)
        },
        render () {
        },
        methods: {
            setTitle(title) {
                document.title = title
            }
        }
    }
</script>

<style scoped>

</style>