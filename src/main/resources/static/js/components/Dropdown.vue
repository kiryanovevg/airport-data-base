<template>
    <div class="mb-3">
        <app-loading :loading="loading"/>
        <div class="input-group"
             v-if="!loading"
        >
            <div class="input-group-prepend">
                <label class="input-group-text" for="selectedInput">{{ title }}</label>
            </div>
            <div class="custom-select" id="selectedInput"
                 v-bind:data-toggle="(items != null && items.length !== 0) ? 'dropdown': ''"
            >
                <div class="d-flex align-items-center title-block"
                     v-if="selected"
                >
                    {{ fill(selected) }}
                </div>
            </div>

            <ul class="dropdown-menu dropdown-search-box">
                <li class="search-box"><input type="text" class="form-control" placeholder="Search" v-model="search"></li>
                <li
                        class="list-group-item list-group-item-action"
                        v-for="(item, index) in filtered"
                        v-on:click="selectItem(index)"
                >
                    {{ fill(item) }}
                </li>
            </ul>
        </div>
    </div>
</template>
<!--
<app-dropdown :title="'Title'"
              :loading="loading"
              :items="items"
              :fill="item => item"
              v-model="input"
/>
-->
<script>
    export default {
        props: ['title', 'items', 'loading', 'fill', 'value'],
        name: "Dropdown",
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
            selectItem(index) {
                this.$emit('input', this.filtered[index]);
                this.selected = null;
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
    ul.dropdown-search-box {
        max-height: 300px;
        width: 270px;
        overflow-y: auto;
    }
    ul.dropdown-search-box .search-box {
        padding:  5px 5px 5px;
        border-bottom: 1px solid #ddd;
    }
    .title-block {
        white-space: nowrap;      /*keep text on one line */
        overflow: hidden;         /*prevent text from being shown outside the border */
        text-overflow: ellipsis;  /*cut off text with an ellipsis*/
    }
</style>