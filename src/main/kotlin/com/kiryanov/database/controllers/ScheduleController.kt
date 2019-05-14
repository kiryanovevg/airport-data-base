package com.kiryanov.database.controllers

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

    @GetMapping
    fun getAllSchedule(): List<Schedule> {
        return scheduledService.getAllSchedule()
    }

    @PostMapping
    fun addSchedule(@RequestBody dto: HashMap<String, String>?): Schedule {
        return scheduledService.addSchedule(dto)
    }

    @DeleteMapping("/{id}")
    fun deleteDirection(@PathVariable("id") id: Long): ResponseEntity<String> {
        scheduledService.deleteSchedule(id)
        return ResponseEntity.ok("Successful deleted")
    }
}