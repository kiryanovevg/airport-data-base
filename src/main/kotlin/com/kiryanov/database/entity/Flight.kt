package com.kiryanov.database.entity

import org.hibernate.annotations.GenericGenerator
import org.springframework.scheduling.annotation.Scheduled
import javax.persistence.*

@Entity
@Table(name = "flight")
data class Flight(

        @Column(nullable = false)
        val price: Int,

        @OneToOne
        @JoinColumn(name = "schedule_id")
        val schedule: Schedule,

        @ManyToOne
        @JoinColumn(name = "direction_id")
        val direction: Direction,

        @ManyToOne
        @JoinColumn(name = "airplane_id")
        val airplane: Airplane,

        @OneToMany(mappedBy = "flight", fetch = FetchType.EAGER)
        val tickets: List<Ticket>? = null,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
) {
        data class DTO internal constructor(
                val id: Long = 0,
                val price: Int,
                val schedule: Long,
                val direction: Long,
                val airplane: Long
        )

        fun getDTO() = DTO(
                id, price, schedule.id, direction.id, airplane.id
        )
}