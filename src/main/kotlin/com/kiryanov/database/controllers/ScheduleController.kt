package com.kiryanov.database.controllers

import com.fasterxml.jackson.annotation.JsonView
import com.kiryanov.database.entity.Flight
import com.kiryanov.database.entity.Schedule
import com.kiryanov.database.services.ScheduleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/schedule")
class ScheduleController {

    @Autowired
    private lateinit var scheduledService: ScheduleService

    private interface Rest:
            Schedule.ID,
            Schedule.Departure,
            Schedule.Arrival,
            Schedule.Flight,
            Flight.ID

    @JsonView(Rest::class)
    @GetMapping
    fun getAllSchedule(): List<Schedule> {
        return scheduledService.getAllSchedule()
    }

    @JsonView(Rest::class)
    @PostMapping
    fun addSchedule(@RequestBody dto: Schedule?): Schedule {
        return scheduledService.addSchedule(dto)
    }

    /*@JsonView(Rest::class)
    @PostMapping
    fun addSchedule(@RequestBody dto: HashMap<String, String>?): Schedule {
        return scheduledService.addSchedule(dto)
    }*/

    @DeleteMapping("/{id}")
    fun deleteDirection(@PathVariable("id") id: Long): ResponseEntity<String> {
        scheduledService.deleteSchedule(id)
        return ResponseEntity.ok("Successful deleted")
    }
}