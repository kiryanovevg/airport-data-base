<template>
    <div>
        <hr>

        <app-loading :loading="loading"/>

        <ul class="list-group scrollable"
            v-if="!loading"
        >
            <li
                    class="list-group-item list-group-item-action"
                    v-for="(item, index) in items"
                    v-on:click="select(index)"
                    v-bind:class="{ active: item === selected }"
            >
                {{index + 1}} - {{ fill(item) }}
            </li>
        </ul>

        <hr>
    </div>
</template>
<!--
<app-list :loading="loading"
          :items="items"
          v-model="model"
          :fill="item => item"
/>
-->
<script>
    export default {
        name: "List",
        props: ['items', 'loading', 'fill', 'value'],
        data() {
            return {
                selected: null,
            }
        },
        watch: {
            value() {
                this.selected = this.value
            }
        },
        methods: {
            select(index) {
                this.$emit('input', this.items[index])
            }
        }
    }
</script>

<style scoped>
    /*.list-group{
        max-height: 300px;
        overflow:auto;
        -webkit-overflow-scrolling: touch;
    }*/
    .scrollable {
        max-height: 300px;
        overflow:auto;
        -webkit-overflow-scrolling: touch;
    }
</style>