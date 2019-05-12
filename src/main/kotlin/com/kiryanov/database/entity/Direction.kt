package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "direction")
data class Direction(

        @ManyToOne
        @JoinColumn(name = "from_city_id")
        val fromCity: City,

        @ManyToOne
        @JoinColumn(name = "to_city_id")
        val toCity: City,

        @OneToMany(mappedBy = "direction", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
        val flight: List<Flight>? = null,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
        data class DTO internal constructor(
                val id: Long = 0,
                val fromCityId: Long,
                val toCityId: Long
        )

        fun getDTO() = DTO(
                id, fromCity.id, toCity.id
        )
}