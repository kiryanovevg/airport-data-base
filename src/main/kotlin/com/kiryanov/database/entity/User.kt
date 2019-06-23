package com.kiryanov.database.entity

import com.fasterxml.jackson.annotation.JsonView
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user_table")
data class User(

        @JsonView(Login::class)
        @Column(nullable = false)
        val login: String,

        @JsonView(Password::class)
        @Column(nullable = false)
        val password: String,

        @JsonView(Role::class)
        @Column(nullable = false)
        val userRole: String,

        @JsonView(ID::class)
        @Id
//        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false, updatable = false)
        val id: Long
) {
        interface Login
        interface Password
        interface ID
        object Role {
                const val ADMIN = "Admin"
                const val DISPATCHER = "Dispatcher"
                const val CASHIER = "Cashier"
                const val SECURITY = "Security"
        }
}