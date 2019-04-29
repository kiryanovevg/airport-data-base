package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "city")
data class City(
        @Column(nullable = false)
        val name: String,

        @OneToMany(mappedBy = "fromCity", fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.REMOVE])
        val fromCities: List<Direction>,

        @OneToMany(mappedBy = "toCity", fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.REMOVE])
        val toCities: List<Direction>,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
    data class DTO internal constructor(
            val id: Long = 0,
            val name: String
    )

    fun getDTO() = DTO(
            id, name
    )
}