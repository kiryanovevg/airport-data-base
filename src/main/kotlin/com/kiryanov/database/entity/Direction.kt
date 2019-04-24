package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "direction")
data class Direction(

        @Column(nullable = false)
        val fromCity: String,

        @Column(nullable = false)
        val toCity: String,

        @OneToOne(mappedBy = "direction", fetch = FetchType.EAGER)
        val flight: Flight,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
)