package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.Flight
import com.kiryanov.database.entity.Schedule
import com.kiryanov.database.entity.Ticket
import com.kiryanov.database.repositories.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TicketService {

    @Autowired
    private lateinit var ticketRepository: TicketRepository

    @Autowired
    private lateinit var flightRepository: FlightRepository

    fun getAll(): List<Ticket> = ticketRepository
            .findAll()

    fun findById(id: Long): Ticket = ticketRepository
            .findByIdOrNull(id)
            ?: throw RestException("Ticket not found")

    fun addTicket(dto: Ticket.DTO?): Ticket {
        if (dto != null) {
            if (dto.flight == 0L
                    || dto.place == 0)
                throw RestException("Заполните все данные")

            val flight = flightRepository
                    .findByIdOrNull(dto.flight)
                    ?: throw RestException("Flight id not found!")

            if (flight.tickets.size >= flight.airplane.capacity)
                throw RestException("Нет свободных мест на этот рейс")

            val ticket = Ticket(dto.luggage, dto.place, flight)
            return ticketRepository.save(ticket)
        } else throw RestException("Введите данные")
    }

    fun deleteFlight(id: Long) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id)
        } else throw RestException("Not Found")
    }
}