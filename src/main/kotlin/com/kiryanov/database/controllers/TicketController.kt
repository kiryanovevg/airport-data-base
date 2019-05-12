package com.kiryanov.database.controllers

import com.kiryanov.database.entity.Ticket
import com.kiryanov.database.services.TicketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tickets")
class TicketController {

    @Autowired
    private lateinit var ticketService: TicketService

    @GetMapping
    fun getAllTicket(): List<Ticket.DTO> {
        return ticketService.getAll()
                .map { it.getDTO() }
    }

    @PostMapping
    fun addFlight(@RequestBody dto: Ticket.DTO?): Ticket.DTO {
        return ticketService.addTicket(dto).getDTO()
    }

    @DeleteMapping("/{id}")
    fun deleteFlight(@PathVariable("id") id: Long): ResponseEntity<String> {
        ticketService.deleteFlight(id)
        return ResponseEntity.ok("Successful deleted")
    }

}