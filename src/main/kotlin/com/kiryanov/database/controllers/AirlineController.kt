package com.kiryanov.database.controllers

import com.fasterxml.jackson.annotation.JsonView
import com.kiryanov.database.entity.Airline
import com.kiryanov.database.entity.Airplane
import com.kiryanov.database.services.AirlineService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/airlines")
class AirlineController {

    @Autowired
    private lateinit var airlineService: AirlineService

    private interface Rest:
            Airline.ID,
            Airline.Name,
            Airline.Airplanes,
            Airplane.ID,
            Airplane.Capacity,
            Airplane.Model
//            Airplane.Flights

    @JsonView(Rest::class)
    @GetMapping
    fun getAllAirlines(): List<Airline> {
        return airlineService.getAll()
    }

    @JsonView(Rest::class)
    @GetMapping("/{id}")
    fun getAirline(@PathVariable("id") id: Long): Airline {
        return airlineService.findById(id)
    }

    @JsonView(Rest::class)
    @PostMapping
    fun addAirline(@RequestBody airline: HashMap<String, String>?): Airline {
        return airlineService.addAirline(airline)
    }

    @DeleteMapping("/{id}")
    fun deleteAirline(@PathVariable("id") id: Long): ResponseEntity<String> {
        airlineService.deleteAirline(id)
        return ResponseEntity.ok("Successful deleted")
    }
}