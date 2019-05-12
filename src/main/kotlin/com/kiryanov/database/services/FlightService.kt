package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.Flight
import com.kiryanov.database.entity.Schedule
import com.kiryanov.database.repositories.AirplaneRepository
import com.kiryanov.database.repositories.DirectionRepository
import com.kiryanov.database.repositories.FlightRepository
import com.kiryanov.database.repositories.ScheduleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FlightService {

    @Autowired
    private lateinit var flightRepository: FlightRepository
    @Autowired
    private lateinit var airplaneRepository: AirplaneRepository
    @Autowired
    private lateinit var directionRepository: DirectionRepository
    @Autowired
    private lateinit var scheduleRepository: ScheduleRepository

    fun getAll(): List<Flight> = flightRepository
            .findAll()

    fun findById(id: Long): Flight = flightRepository
            .findByIdOrNull(id)
            ?: throw RestException("Flight not found")

    fun addFlight(dto: Flight.DTO?): Flight {
        if (dto != null) {
            if (dto.price == 0
                    || dto.airplane == 0L
                    || dto.direction == 0L
                    || dto.schedule == 0L)
                throw RestException("Заполните все данные")
            if (dto.price < 0) throw RestException("Цена не может быть меньше нуля")

            val airplane = airplaneRepository
                    .findByIdOrNull(dto.airplane)
                    ?: throw RestException("Airplane id not found!")

            val direction = directionRepository
                    .findByIdOrNull(dto.direction)
                    ?: throw RestException("Direction id not found!")

            val schedule = scheduleRepository
                    .findByIdOrNull(dto.schedule)
                    ?: throw RestException("Schedule id not found!")

            if (schedule.flight != null) throw RestException("Schedule already using!")
            val flight = Flight(dto.price, schedule, direction, airplane)
            return flightRepository.save(flight)
        } else throw RestException("Введите данные")
    }

    fun deleteFlight(id: Long) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id)
        } else throw RestException("Not Found")
    }
}