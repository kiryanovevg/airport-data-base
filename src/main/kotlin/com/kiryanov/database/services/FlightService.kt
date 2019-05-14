package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.City
import com.kiryanov.database.entity.Flight
import com.kiryanov.database.getValueSafety
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
    private lateinit var airplaneService: AirplaneService
    @Autowired
    private lateinit var directionService: DirectionService
    @Autowired
    private lateinit var scheduleService: ScheduleService

    fun getAll(): List<Flight> = flightRepository
            .findAll()

    fun findById(id: Long): Flight = flightRepository
            .findByIdOrNull(id)
            ?: throw RestException("City not found")

    fun getFlightWithEmptyPlaces(): List<Flight> {
        return flightRepository.findAll().filter { flight ->
            val capacity = flight.airplane.capacity
            val ticketCount = flight.tickets.size

            ticketCount < capacity
        }
    }

    fun getFreePlaces(id: Long): List<Int> {
        val flight = findById(id)
        val placesList = flight.tickets.map { ticket -> ticket.place }
        val result = (1..flight.airplane.capacity).toList()

        return result.filter { !placesList.contains(it) }
    }

    fun addFlight(dto: HashMap<String, String>?): Flight = dto?.let { map ->
        val price = map.getValueSafety("price").toInt()
        val airplane = airplaneService.findById(map.getValueSafety("airplane").toLong())
        val direction = directionService.findById(map.getValueSafety("direction").toLong())
        val schedule = scheduleService.findById(map.getValueSafety("schedule").toLong())

        if (schedule.flight != null) throw RestException("Schedule already using!")

        return flightRepository.save(Flight(price, schedule, direction, airplane, emptyList()))
    } ?: throw RestException("Empty RequestBody")

    fun deleteFlight(id: Long) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id)
        } else throw RestException("Not Found")
    }
}