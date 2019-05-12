package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "ticket")
data class Ticket(

        @Column(nullable = false)
        val luggage: Boolean,

        @Column(nullable = false)
        val place: Int,

        @ManyToOne(optional = false)
        @JoinColumn(name = "flight_id")
        val flight: Flight,

        @OneToOne(mappedBy = "ticket", fetch = FetchType.EAGER)
        val passenger: Passenger? = null,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
        data class DTO internal constructor(
                val id: Long = 0,
                val flight: Long,
                val luggage: Boolean,
                val place: Int
        )

        fun getDTO() = DTO(
                id, flight.id, luggage, place
        )
}