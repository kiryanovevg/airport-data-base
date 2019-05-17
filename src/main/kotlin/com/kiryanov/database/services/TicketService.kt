package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.Flight
import com.kiryanov.database.entity.Schedule
import com.kiryanov.database.entity.Ticket
import com.kiryanov.database.getValueSafety
import com.kiryanov.database.repositories.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TicketService {

    @Autowired
    private lateinit var ticketRepository: TicketRepository

    @Autowired
    private lateinit var flightService: FlightService

    @Autowired
    private lateinit var passengerService: PassengerService

    fun getAll(): List<Ticket> = ticketRepository
            .findAll()

    fun findById(id: Long): Ticket = ticketRepository
            .findByIdOrNull(id)
            ?: throw RestException("Ticket not found")

    fun addTicket(dto: HashMap<String, String>?): Ticket = dto?.let { map ->
        val passenger = passengerService.findById(map.getValueSafety("passenger").toLong())
        val flight = flightService.findById(map.getValueSafety("flight").toLong())
        val place = map.getValueSafety("place").toInt()
        val luggage = map.getValueSafety("luggage").toInt()

        if (ticketRepository.findAll().firstOrNull { it.luggage == luggage } != null)
            throw RestException("Багаж с таким номером уже существует")
        if (place <= 0) throw RestException("Неверное место")
        if (flight.tickets.size >= flight.airplane.capacity)
            throw RestException("Нет свободных мест на этот рейс")

        return ticketRepository.save(Ticket(luggage, place, flight, passenger))
    } ?: throw RestException("Empty RequestBody")

    fun delete(id: Long) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id)
        } else throw RestException("Not Found")
    }
}