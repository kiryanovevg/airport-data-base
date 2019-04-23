package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "schedule")
data class Schedule(
        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long,

        @Temporal(TemporalType.TIMESTAMP)
        @Column(nullable = false)
        val departure: Date,

        @Temporal(TemporalType.TIMESTAMP)
        @Column(nullable = false)
        val arrival: Date,

        @OneToOne(mappedBy = "schedule", fetch = FetchType.EAGER)
        val flight: Flight
)