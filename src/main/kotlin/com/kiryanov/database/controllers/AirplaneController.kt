package com.kiryanov.database.controllers

import com.kiryanov.database.entity.Airplane
import com.kiryanov.database.services.AirlineService
import com.kiryanov.database.services.AirplaneService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/airplanes")
class AirplaneController {

    @Autowired
    private lateinit var airplaneService: AirplaneService

    @Autowired
    private lateinit var airlineService: AirlineService

    @GetMapping
    fun getAllAirplanes(): List<Airplane.DTO> {
        return airplaneService.getAll()
                .map { it.getDTO() }
    }

    @GetMapping("/{id}")
    fun getAirplane(@PathVariable("id") id: Long): Airplane.DTO {
        return airplaneService.findById(id).getDTO()
    }

    @PostMapping
    fun addAirplanes(@RequestBody dto: Airplane.DTO?): Airplane.DTO {
        return if (dto != null) {
            val airline = airlineService.findById(dto.airlineId)
            airplaneService.addAirplane(dto, airline).getDTO()
        } else throw RestException("Data error")
    }

    @DeleteMapping("/{id}")
    fun deleteAirplanes(@PathVariable("id") id: Long): ResponseEntity<String> {
        airplaneService.deleteAirplane(id)
        return ResponseEntity.ok("Successful deleted")
    }
}