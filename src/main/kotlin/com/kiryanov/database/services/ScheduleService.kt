package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.Schedule
import com.kiryanov.database.repositories.ScheduleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScheduleService {

    @Autowired
    private lateinit var scheduleRepository: ScheduleRepository

    fun getAllSchedule(): List<Schedule> = scheduleRepository
            .findAll()

    fun addSchedule(dto: Schedule.DTO?): Schedule {
        if (dto != null) {
            if (dto.departure.time < dto.arrival.time) {
                val schedule = Schedule(dto.departure, dto.arrival)
                return scheduleRepository.save(schedule)
            } else throw RestException("Неверное расписание")
        } else throw RestException("Введите данные")
    }

    fun deleteSchedule(id: Long) {
        if (scheduleRepository.existsById(id)) {
            scheduleRepository.deleteById(id)
        } else throw RestException("Not Found")
    }
}