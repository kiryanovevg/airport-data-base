package com.kiryanov.database.entity

import com.fasterxml.jackson.annotation.JsonView
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "airline")
data class Airline(

        @JsonView(Name::class)
        @Column(nullable = false)
        val name: String,

        @JsonView(Airplanes::class)
        @OneToMany(mappedBy = "airline", fetch = FetchType.EAGER, orphanRemoval = true, cascade = [CascadeType.REMOVE])
        val airplanes: List<Airplane>,

        @JsonView(ID::class)
        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
    interface Name
    interface Airplanes
    interface ID
}