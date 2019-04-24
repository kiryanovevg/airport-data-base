package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "ticket")
data class Ticket(

        @Column(nullable = false)
        val luggage: String,

        @Column(nullable = false)
        val price: Int,

        @Column(nullable = false)
        val place: Int,

        @ManyToOne(optional = false, cascade = [CascadeType.ALL])
        @JoinColumn(name = "flight_id")
        val flight: Flight,

        @OneToOne(mappedBy = "ticket", fetch = FetchType.EAGER)
        val passenger: Passenger,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
)