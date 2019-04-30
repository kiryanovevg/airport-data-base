package com.kiryanov.database.controllers

import com.kiryanov.database.entity.City
import com.kiryanov.database.entity.Direction
import com.kiryanov.database.services.DirectionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/directions")
class DirectionController {

    @Autowired
    private lateinit var directionService: DirectionService

    @GetMapping
    fun getAllDirections(): List<Direction.DTO> {
        return directionService.getAllDirections()
                .map { it.getDTO() }
    }

    @GetMapping("/cities")
    fun getAllCities(): List<City.DTO> {
        return directionService.getAllCities()
                .map { it.getDTO() }
    }

    @PostMapping("/cities")
    fun addCity(@RequestBody dto: City.DTO?): City.DTO {
        return directionService.addCity(dto).getDTO()
    }

    @DeleteMapping("/cities/{id}")
    fun deleteCity(@PathVariable("id") id: Long): ResponseEntity<String> {
        directionService.deleteCity(id)
        return ResponseEntity.ok("Successful deleted")
    }

    @PostMapping
    fun addDirection(@RequestBody dto: Direction.DTO?): Direction.DTO {
        return if (dto != null) {
            val fromCity = directionService.findCityById(dto.fromCityId)
            val toCity = directionService.findCityById(dto.toCityId)

            directionService.addDirection(fromCity, toCity).getDTO()
        } else throw RestException("Data error")
    }

    @DeleteMapping("/{id}")
    fun deleteDirection(@PathVariable("id") id: Long): ResponseEntity<String> {
        directionService.deleteDirection(id)
        return ResponseEntity.ok("Successful deleted")
    }
}