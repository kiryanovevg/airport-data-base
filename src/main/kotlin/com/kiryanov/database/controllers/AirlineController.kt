package com.kiryanov.database.controllers

import com.kiryanov.database.entity.Airline
import com.kiryanov.database.services.AirlineService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/airlines")
class AirlineController {

    @Autowired
    private lateinit var airlineService: AirlineService

    @GetMapping
    fun getAllAirlines(): List<Airline.DTO> {
        return airlineService.getAll()
                .map { it.getDTO() }
    }

    @GetMapping("/{id}")
    fun getAirline(@PathVariable("id") id: Long): Airline.DTO {
        return airlineService.findById(id).getDTO()
    }

    @PostMapping
    fun addAirline(@RequestBody airline: Airline.DTO?): Airline.DTO {
        return airlineService.addAirline(airline).getDTO()
    }

    @DeleteMapping("/{id}")
    fun deleteAirline(@PathVariable("id") id: Long): ResponseEntity<String> {
        airlineService.deleteAirline(id)
        return ResponseEntity.ok("Successful deleted")
    }
}