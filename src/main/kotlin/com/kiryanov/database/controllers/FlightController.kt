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
    fun getAllFlight(
            @RequestParam(value = "free_places", defaultValue = "false") emptyPlaces: Boolean
    ): List<Flight.DTO> {
        return if (emptyPlaces) {
            flightService.getFlightWithEmptyPlaces().map { it.getDTO() }
        } else {
            flightService.getAll().map { it.getDTO() }
        }
    }

    @GetMapping("/{id}/places")
    fun getFreePlaces(@PathVariable("id") id: Long): List<Int> {
        return flightService.getFreePlaces(id)
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