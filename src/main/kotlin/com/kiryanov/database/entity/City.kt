package com.kiryanov.database.entity

import com.fasterxml.jackson.annotation.JsonView
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "city")
data class City(

        @JsonView(Name::class)
        @Column(nullable = false)
        val name: String,

        @JsonView(FromDirections::class)
        @OneToMany(mappedBy = "fromCity", fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.REMOVE])
        val fromDirections: List<Direction>,

        @JsonView(ToDirections::class)
        @OneToMany(mappedBy = "toCity", fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.REMOVE])
        val toDirections: List<Direction>,

        @JsonView(ID::class)
        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
        interface Name
        interface FromDirections
        interface ToDirections
        interface ID
}