package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "airline")
data class Airline(

        @Column(nullable = false)
        val name: String,

        @OneToMany(mappedBy = "airline", fetch = FetchType.EAGER)
        val airplanes: List<Airplane>,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
)