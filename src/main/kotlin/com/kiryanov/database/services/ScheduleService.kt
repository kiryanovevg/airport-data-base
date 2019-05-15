package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.Schedule
import com.kiryanov.database.repositories.ScheduleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ScheduleService {

    @Autowired
    private lateinit var scheduleRepository: ScheduleRepository

    fun getAllSchedule(): List<Schedule> = scheduleRepository
            .findAll()

    fun addSchedule(dto: Schedule?): Schedule = dto?.let { schedule ->
        //2019-05-15T19:49:00.000Z
        val departure = schedule.departure
        val arrival = schedule.arrival

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