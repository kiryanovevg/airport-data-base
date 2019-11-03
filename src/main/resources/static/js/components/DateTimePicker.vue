<template>
    <VueCtkDateTimePicker
            v-model='raw'
            :format="dateFormat"
            :label="label"
    />
</template>

<script>
    import VueCtkDateTimePicker from 'vue-ctk-date-time-picker';
    import 'vue-ctk-date-time-picker/dist/vue-ctk-date-time-picker.css';

    export default {
        props: ['value', 'label'],
        name: "DateTimePicker",
        components: {
            'VueCtkDateTimePicker': VueCtkDateTimePicker
        },
        data() {
            return {
                raw: null,
                dateFormat: 'YYYY-MM-DD HH:mm'
            }
        },
        watch: {
            raw() {
                this.$emit('input', new Date(Date.parse(this.raw)))
            }
        },
        computed: {

        },
        methods: {

        }
    }
</script>

<style scoped>

</style>