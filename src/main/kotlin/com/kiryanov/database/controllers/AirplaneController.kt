package com.kiryanov.database.controllers

import com.fasterxml.jackson.annotation.JsonView
import com.kiryanov.database.entity.Airline
import com.kiryanov.database.entity.Airplane
import com.kiryanov.database.services.AirplaneService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/airplanes")
class AirplaneController {

    @Autowired
    private lateinit var airplaneService: AirplaneService

    private interface Rest:
            Airplane.ID,
            Airplane.Model,
            Airplane.Capacity,
            Airplane.Airline,
            Airline.ID,
            Airline.Name

    @JsonView(Rest::class)
    @GetMapping
    fun getAllAirplanes(): List<Airplane> {
        return airplaneService.getAll()
    }

    @JsonView(Rest::class)
    @GetMapping("/{id}")
    fun getAirplane(@PathVariable("id") id: Long): Airplane {
        return airplaneService.findById(id)
    }

    @JsonView(Rest::class)
    @PostMapping
    fun addAirplanes(@RequestBody dto: HashMap<String, String>?): Airplane {
        return airplaneService.addAirplane(dto)
    }

    @DeleteMapping("/{id}")
    fun deleteAirplanes(@PathVariable("id") id: Long): ResponseEntity<String> {
        airplaneService.deleteAirplane(id)
        return ResponseEntity.ok("Successful deleted")
    }
}