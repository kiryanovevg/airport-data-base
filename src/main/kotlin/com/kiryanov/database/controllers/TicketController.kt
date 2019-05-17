package com.kiryanov.database.controllers

import com.fasterxml.jackson.annotation.JsonView
import com.kiryanov.database.entity.*
import com.kiryanov.database.services.TicketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tickets")
class TicketController {

    @Autowired
    private lateinit var ticketService: TicketService

    private interface Rest:
            Ticket.ID,
            Ticket.Place,
            Ticket.Flight,
            Ticket.Passenger,
            Ticket.Luggage,
            Passenger.ID,
            Passenger.Surname,
            Passenger.Name,
            Passenger.Patronymic,
            Passenger.Series,
            Passenger.Number,
            Flight.ID,
            Flight.Price,
            Flight.Airplane,
            Flight.Direction,
            Flight.Schedule,
            Airplane.Model,
            Direction.ID,
            Direction.FromCity,
            Direction.ToCity,
            City.Name,
            Schedule.Arrival,
            Schedule.Departure,
            Schedule.ID

    @JsonView(Rest::class)
    @GetMapping
    fun getAllTicket(): List<Ticket> {
        return ticketService.getAll()
    }

    @JsonView(Rest::class)
    @PostMapping
    fun addTicket(@RequestBody dto: HashMap<String, String>?): Ticket {
        return ticketService.addTicket(dto)
    }

    @DeleteMapping("/{id}")
    fun deleteTicket(@PathVariable("id") id: Long): ResponseEntity<String> {
        ticketService.delete(id)
        return ResponseEntity.ok("Successful deleted")
    }

}