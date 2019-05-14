package com.kiryanov.database.entity

import com.fasterxml.jackson.annotation.JsonView
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "schedule")
data class Schedule(

        @JsonView(Departure::class)
        @Temporal(TemporalType.TIMESTAMP)
        @Column(nullable = false)
        val departure: Date,

        @JsonView(Arrival::class)
        @Temporal(TemporalType.TIMESTAMP)
        @Column(nullable = false)
        val arrival: Date,

        @JsonView(Flight::class)
        @OneToOne(mappedBy = "schedule", fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
        val flight: com.kiryanov.database.entity.Flight? = null,

        @JsonView(ID::class)
        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
        interface Departure
        interface Arrival
        interface Flight
        interface ID
}