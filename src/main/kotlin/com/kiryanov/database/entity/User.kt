package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "user_table")
data class User(

        @Column(nullable = false)
        val login: String,

        @Column(nullable = false)
        val password: String,

        @Column(nullable = false)
        val adminPermission: Boolean,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
)