package com.kiryanov.database.entity

import com.fasterxml.jackson.annotation.JsonView
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "passenger")
data class Passenger(

        @JsonView(Surname::class)
        @Column(nullable = false)
        val surname: String,

        @JsonView(Name::class)
        @Column(nullable = false)
        val name: String,

        @JsonView(Patronymic::class)
        @Column(nullable = false)
        val patronymic: String,

        @JsonView(Series::class)
        @Column(nullable = false)
        val series: Int,

        @JsonView(Number::class)
        @Column(nullable = false)
        val number: Int,

        @JsonView(Tickets::class)
        @OneToMany(mappedBy = "passenger", fetch = FetchType.EAGER, orphanRemoval = true, cascade = [CascadeType.REMOVE])
        val tickets: List<Ticket> = emptyList(),

        @JsonView(ID::class)
        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
        interface ID
        interface Surname
        interface Name
        interface Patronymic
        interface Series
        interface Number
        interface Tickets
}