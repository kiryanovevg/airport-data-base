<template>
    <div class="container pt-5 pb-5">

        <div class="row">
            <div class="col-md-auto mt-2 d-flex justify-content-center"
                 v-for="(link, index) in links"
            >
<!--                :class="{ 'mr-3': (index !== links.length - 1)}"-->
                <router-link class="box btn card text-center bg-light"
                             style="max-width: 18rem;"
                             :to="link.path"
                >
                    <div class="card-body">
                        <p class="card-text">
                            {{ link.name }}
                        </p>
                    </div>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Main",
        data() {
            return {
                links: [
                    {name: 'Airlines', path: '/airlines'},
                    {name: 'Airplanes', path: '/airplanes'},
                    {name: 'Directions', path: '/directions'},
                    {name: 'Schedule', path: '/schedule'},
                    {name: 'Flights', path: '/flights'},
                ]
            }
        }
    }
</script>

<style>
    .box {
        position: relative;
        display: inline-block;
        width: auto;
        height: auto;
        /*color: white;
        background-color: #5d5d5d;*/
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        border-radius: 5px;
        -webkit-transition: all 0.6s cubic-bezier(0.165, 0.84, 0.44, 1);
        transition: all 0.6s cubic-bezier(0.165, 0.84, 0.44, 1);
    }

    .box::after {
        content: "";
        border-radius: 5px;
        position: absolute;
        z-index: -1;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
        opacity: 0;
        -webkit-transition: all 0.6s cubic-bezier(0.165, 0.84, 0.44, 1);
        transition: all 0.6s cubic-bezier(0.165, 0.84, 0.44, 1);
    }

    .box:hover {
        -webkit-transform: scale(1.05, 1.05);
        transform: scale(1.05, 1.05);
    }

    .box:hover::after {
        opacity: 1;
    }

</style>