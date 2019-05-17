<template>
    <div>
        <hr>

        <app-loading :loading="loading"/>

        <div v-if="!loading">
            <div class="input-group mb-3">
                <input type="text" class="form-control" placeholder="Search" aria-label="Search" aria-describedby="basic-addon2"
                       v-model="search"
                >
                <div class="input-group-append">
                    <button class="btn btn-outline-secondary" type="button" v-on:click="clear">Clear</button>
                </div>
            </div>

            <ul class="list-group scrollable">
                <li
                        class="list-group-item list-group-item-action"
                        v-for="(item, index) in filtered"
                        v-on:click="select(index)"
                        v-bind:class="{ active: item === selected }"
                >
                    {{items.indexOf(item) + 1}} - {{ fill(item) }}
                </li>
            </ul>
        </div>

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
                search: null,
            }
        },
        watch: {
            value() {
                this.selected = this.value
            }
        },
        computed: {
            filtered() {
                const items = this.items;
                const search = this.search;

                if (items){
                    if (!search) return this.items;
                    return items.filter(value => {
                        return contains(value, search);
                    });
                }
            }
        },
        methods: {
            select(index) {
                this.$emit('input', this.filtered[index])
            },

            clear() {
                this.search = null;
            }
        }
    }

    function contains(value, search) {
        let result = false;
        Object.keys(value).forEach(key => {
            if (value[key] != null) {
                if (value[key] instanceof Date) {

                } else if (value[key] instanceof Object) {
                    if (!result) result = contains(value[key], search);
                } else if (!result) result = value[key]
                    .toString()
                    .toLowerCase()
                    .includes(search.toLowerCase());
            }
        });
        return result;
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