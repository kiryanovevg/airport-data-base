package com.kiryanov.database.controllers

import com.fasterxml.jackson.annotation.JsonView
import com.kiryanov.database.entity.City
import com.kiryanov.database.entity.Direction
import com.kiryanov.database.services.DirectionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/directions")
class DirectionController {

    @Autowired
    private lateinit var directionService: DirectionService

    private interface DirectionRest:
            Direction.ID,
            Direction.FromCity,
            Direction.ToCity,
            CityRest

    private interface CityRest:
            City.ID,
            City.Name


    @JsonView(DirectionRest::class)
    @GetMapping
    fun getAllDirections(): List<Direction> {
        return directionService.getAllDirections()
    }

    @JsonView(CityRest::class)
    @GetMapping("/cities")
    fun getAllCities(): List<City> {
        return directionService.getAllCities()
    }

    @JsonView(CityRest::class)
    @PostMapping("/cities")
    fun addCity(@RequestBody dto: HashMap<String, String>): City {
        return directionService.addCity(dto)
    }

    @JsonView(DirectionRest::class)
    @PostMapping
    fun addDirection(@RequestBody dto: HashMap<String, String>): Direction {
        return directionService.addDirection(dto)
    }

    @DeleteMapping("/cities/{id}")
    fun deleteCity(@PathVariable("id") id: Long): ResponseEntity<String> {
        directionService.deleteCity(id)
        return ResponseEntity.ok("Successful deleted")
    }

    @DeleteMapping("/{id}")
    fun deleteDirection(@PathVariable("id") id: Long): ResponseEntity<String> {
        directionService.deleteDirection(id)
        return ResponseEntity.ok("Successful deleted")
    }
}