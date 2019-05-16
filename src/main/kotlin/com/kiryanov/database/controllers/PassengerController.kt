package com.kiryanov.database.controllers

import com.fasterxml.jackson.annotation.JsonView
import com.kiryanov.database.entity.Passenger
import com.kiryanov.database.services.PassengerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/passenger")
class PassengerController {

    @Autowired
    private lateinit var passengerService: PassengerService

    private interface Rest:
            Passenger.ID,
            Passenger.Surname,
            Passenger.Name,
            Passenger.Patronymic,
            Passenger.Series,
            Passenger.Number

    @JsonView(Rest::class)
    @GetMapping
    fun getAll(): List<Passenger> {
        return passengerService.getAll()
    }

    @JsonView(Rest::class)
    @PostMapping
    fun add(@RequestBody dto: HashMap<String, String>?): Passenger {
        return passengerService.add(dto)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<String> {
        passengerService.delete(id)
        return ResponseEntity.ok("Successful deleted")
    }

}