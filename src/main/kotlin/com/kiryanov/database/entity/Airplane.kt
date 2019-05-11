package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "airplane")
data class Airplane(

        @Column(nullable = false)
        val model: String,

        @Column(nullable = false)
        val capacity: String,

        @ManyToOne
        @JoinColumn(name = "airline_id")
        val airline: Airline,

        @OneToMany(mappedBy = "airplane", fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
        val flights: List<Flight>? = null,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
    data class DTO internal constructor(
            val id: Long = 0,
            val model: String,
            val capacity: String,
            val airlineId: Long,
            val flights: List<Flight.DTO>?
    )

    fun getDTO() = DTO(
            id, model, capacity, airline.id, flights?.map { it.getDTO() }
    )
}