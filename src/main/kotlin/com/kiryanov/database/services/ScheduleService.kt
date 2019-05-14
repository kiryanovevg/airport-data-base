package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.Flight
import com.kiryanov.database.entity.Schedule
import com.kiryanov.database.getValueSafety
import com.kiryanov.database.repositories.ScheduleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service
class ScheduleService {

    @Autowired
    private lateinit var scheduleRepository: ScheduleRepository

    fun getAllSchedule(): List<Schedule> = scheduleRepository
            .findAll()

    fun addSchedule(dto: HashMap<String, String>?): Schedule = dto?.let { map ->
        val departure = Date(map.getValueSafety("departure"))
        val arrival = Date(map.getValueSafety("arrival"))

        if (departure.time >= arrival.time) throw RestException("Неверное расписание")

        return scheduleRepository.save(Schedule(departure, arrival))
    } ?: throw RestException("Empty RequestBody")

    fun deleteSchedule(id: Long) {
        if (scheduleRepository.existsById(id)) {
            scheduleRepository.deleteById(id)
        } else throw RestException("Not Found")
    }

    fun findById(id: Long): Schedule = scheduleRepository
            .findByIdOrNull(id)
            ?: throw RestException("Schedule not found")
}