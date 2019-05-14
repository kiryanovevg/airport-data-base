package com.kiryanov.database.entity

import com.fasterxml.jackson.annotation.JsonView
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "direction")
data class Direction(

        @JsonView(FromCity::class)
        @ManyToOne
        @JoinColumn(name = "from_city_id")
        val fromCity: City,

        @JsonView(ToCity::class)
        @ManyToOne
        @JoinColumn(name = "to_city_id")
        val toCity: City,

        @JsonView(Flights::class)
        @OneToMany(mappedBy = "direction", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        val flights: List<Flight>,

        @JsonView(ID::class)
        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
        interface FromCity
        interface ToCity
        interface Flights
        interface ID
}