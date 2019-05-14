package com.kiryanov.database.entity

import com.fasterxml.jackson.annotation.JsonView
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "user_table")
data class User(

        @JsonView(Login::class)
        @Column(nullable = false)
        val login: String,

        @JsonView(Password::class)
        @Column(nullable = false)
        val password: String,

        @JsonView(AdminPermission::class)
        @Column(nullable = false)
        val adminPermission: Boolean,

        @JsonView(ID::class)
        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
        interface Login
        interface Password
        interface AdminPermission
        interface ID
}