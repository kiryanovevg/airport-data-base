package com.kiryanov.database.entity

import com.fasterxml.jackson.annotation.JsonView
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "ticket")
data class Ticket(

        @JsonView(Luggage::class)
        @Column(nullable = false)
        val luggage: Boolean,

        @JsonView(Place::class)
        @Column(nullable = false)
        val place: Int,

        @JsonView(Flight::class)
        @ManyToOne(optional = false)
        @JoinColumn(name = "flight_id")
        val flight: com.kiryanov.database.entity.Flight,

        @JsonView(Passenger::class)
        @ManyToOne(optional = false)
        @JoinColumn(name = "passenger_id")
        val passenger: com.kiryanov.database.entity.Passenger,

        @JsonView(ID::class)
        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
        interface Luggage
        interface Place
        interface Flight
        interface Passenger
        interface ID
}