package com.kiryanov.database.entity

import com.fasterxml.jackson.annotation.JsonView
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "airplane")
data class Airplane(

        @JsonView(Model::class)
        @Column(nullable = false)
        val model: String,

        @JsonView(Capacity::class)
        @Column(nullable = false)
        val capacity: Int,

        @JsonView(Airline::class)
        @ManyToOne
        @JoinColumn(name = "airline_id")
        val airline: com.kiryanov.database.entity.Airline,

        @JsonView(Flights::class)
        @OneToMany(mappedBy = "airplane", fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
        val flights: List<Flight>? = null,

        @JsonView(ID::class)
        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
        interface Model
        interface Capacity
        interface Airline
        interface Flights
        interface ID
}