package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "passenger")
data class Passenger(
        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long,

        @Column(nullable = false)
        val surname: String,

        @Column(nullable = false)
        val name: String,

        @Column(nullable = false)
        val patronymic: String,

        @Column(nullable = false)
        val series: Int,

        @Column(nullable = false)
        val number: Int,

        @OneToOne(optional = false, cascade = [CascadeType.ALL])
        @JoinColumn(name = "ticket_id", nullable = false)
        val ticket: Ticket
)