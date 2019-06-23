package com.kiryanov.database.entity

import com.fasterxml.jackson.annotation.JsonView
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "user_temp")
data class UserChanges(

        @JsonView(Login::class)
        @Column(nullable = false)
        val login: String,

        @JsonView(Password::class)
        @Column(nullable = false)
        val password: String,

        @JsonView(Role::class)
        @Column(nullable = false)
        val userRole: String,

        @JsonView(Action::class)
        @Column(nullable = false)
        val action: String,

        @JsonView(UserID::class)
        @Column(nullable = false)
        val userId: Long,

        @JsonView(ID::class)
        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
        interface Login
        interface Password
        interface ID
        interface UserID
        object Role {
                const val ADMIN = "Admin"
                const val DISPATCHER = "Dispatcher"
                const val CASHIER = "Cashier"
                const val SECURITY = "Security"
        }
        object Action {
                const val INSERT = "INSERT"
                const val DELETE = "DELETE"
        }

        fun getUser() = User(login, password, userRole, userId)
}