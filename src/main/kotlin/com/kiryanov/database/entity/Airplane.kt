package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "airplane")
data class Airplane(

        @Column(nullable = false)
        val model: String,

        @Column(nullable = false)
        val capacity: String,

        @ManyToOne(optional = false, cascade = [CascadeType.ALL])
        @JoinColumn(name = "airline_id")
        val airline: Airline,

        @OneToOne(mappedBy = "plane", fetch = FetchType.EAGER)
        val flight: Flight,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
)