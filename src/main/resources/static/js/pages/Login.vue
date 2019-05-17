<template>
    <div>
        <div class="container login-container">
            <div class="row d-flex justify-content-center">
                <div class="col-md-6 login-form-2">
                    <h3>Login to Airport DB</h3>
                    <form>
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="Login" value="" v-model="input.login"/>
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" placeholder="Password" value="" v-model="input.password"/>
                        </div>

                        <div class="d-flex justify-content-center mb-2">
                            <div v-if="message">{{ message }}</div>
                        </div>

                        <div class="d-flex justify-content-center">
                            <input v-if="!loading"
                                   v-on:click="sendLogin"
                                   type="button"
                                   class="btnSubmit"
                                   value="Login"
                            />

                            <div class="spinner-border" role="status"
                                 v-if="loading"
                            >
                                <span class="sr-only">Loading...</span>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import apiLogin from "api/login";

    export default {
        name: "Login",
        data() {
            return {
                input: {
                  login: null,
                  password: null
                },
                loading: false,
                message: ''
            }
        },
        methods: {
            sendLogin() {
                let self = this;
                if (!self.input.login || !self.input.password) {
                    self.message = 'Заполните все поля';
                    return
                }

                const login = self.input.login;
                const password = self.input.password;

                self.message = null;
                self.loading = true;

                apiLogin.login(login, password).then(
                    success => {
                        if (success.body.role) {
                            self.$emit('authenticate', success.body)
                        } else {
                            self.message = 'У пользователя нет прав'
                        }
                        self.loading = false;
                    },

                    error => {
                        self.message = error.bodyText;
                        self.loading = false;
                    }
                );
            }
        }
    }
</script>

<style scoped>
    .login-container{
        margin-top: 5%;
        margin-bottom: 5%;
        color: white;
    }
    .login-form-2{
        padding: 5% 0 0;
        background: #0062cc;
        box-shadow: 0 5px 8px 0 rgba(0, 0, 0, 0.2), 0 9px 26px 0 rgba(0, 0, 0, 0.19);
    }
    .login-form-2 h3{
        text-align: center;
    }
    .login-container form{
        padding: 10% 15%;
    }
    .btnSubmit{
        width: 50%;
        border-radius: 1rem;
        padding: 1.5%;
        border: none;
        cursor: pointer;
    }
    .login-form-2 .btnSubmit{
        font-weight: 600;
        color: #0062cc;
        background-color: #fff;
    }

</style>