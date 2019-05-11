package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "schedule")
data class Schedule(

        @Temporal(TemporalType.TIMESTAMP)
        @Column(nullable = false)
        val departure: Date,

        @Temporal(TemporalType.TIMESTAMP)
        @Column(nullable = false)
        val arrival: Date,

        @OneToOne(mappedBy = "schedule", fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
        val flight: Flight? = null,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
        data class DTO internal constructor(
                val id: Long = 0,
                val departure: Date,
                val arrival: Date
        )

        fun getDTO() = DTO(
                id, departure, arrival
        )

        /*fun getDTO() = HashMap<String, String>().apply {
                putAll(mapOf(
                        "id" to id.toString(),
                        "departure" to departure.toString(),
                        "arrival" to arrival.toString()
                ))
                if (flight != null) put("flight", flight.id.toString())
        }*/
}