package com.kiryanov.database.entity

import com.fasterxml.jackson.annotation.JsonView
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "flight")
data class Flight(

        @JsonView(Price::class)
        @Column(nullable = false)
        val price: Int,

        @JsonView(Schedule::class)
        @OneToOne
        @JoinColumn(name = "schedule_id")
        val schedule: com.kiryanov.database.entity.Schedule,

        @JsonView(Direction::class)
        @ManyToOne
        @JoinColumn(name = "direction_id")
        val direction: com.kiryanov.database.entity.Direction,

        @JsonView(Airplane::class)
        @ManyToOne
        @JoinColumn(name = "airplane_id")
        val airplane: com.kiryanov.database.entity.Airplane,

        @JsonView(Tickets::class)
        @OneToMany(mappedBy = "flight", fetch = FetchType.EAGER)
        val tickets: List<Ticket>,

        @JsonView(ID::class)
        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
        interface ID
        interface Price
        interface Schedule
        interface Direction
        interface Airplane
        interface Tickets
}