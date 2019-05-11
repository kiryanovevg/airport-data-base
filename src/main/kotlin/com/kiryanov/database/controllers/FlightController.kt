package com.kiryanov.database.controllers

import com.kiryanov.database.entity.Flight
import com.kiryanov.database.services.FlightService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/flights")
class FlightController {

    @Autowired
    private lateinit var flightService: FlightService

    @GetMapping
    fun getAllFlight(): List<Flight.DTO> {
        return flightService.getAll()
                .map { it.getDTO() }
    }

    @PostMapping
    fun addFlight(@RequestBody dto: Flight.DTO?): Flight.DTO {
        return flightService.addFlight(dto).getDTO()
    }

    @DeleteMapping("/{id}")
    fun deleteFlight(@PathVariable("id") id: Long): ResponseEntity<String> {
        flightService.deleteFlight(id)
        return ResponseEntity.ok("Successful deleted")
    }
}