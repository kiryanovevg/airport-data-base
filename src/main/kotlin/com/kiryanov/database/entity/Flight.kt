package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import org.springframework.scheduling.annotation.Scheduled
import javax.persistence.*

@Entity
@Table(name = "flight")
data class Flight(

        @OneToOne(optional = false, cascade = [CascadeType.ALL])
        @JoinColumn(name = "schedule_id")
        val schedule: Schedule,

        @ManyToOne
        @JoinColumn(name = "direction_id")
        val direction: Direction,

        @OneToOne(optional = false, cascade = [CascadeType.ALL])
        @JoinColumn(name = "plane_id")
        val plane: Airplane,

        @OneToMany(mappedBy = "flight", fetch = FetchType.EAGER)
        val tickets: List<Ticket>,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
)