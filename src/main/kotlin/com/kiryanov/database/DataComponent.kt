package com.kiryanov.database

import com.kiryanov.database.entity.*
import com.kiryanov.database.repositories.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class DataComponent @Autowired constructor(
        private val airlineRepository: AirlineRepository,
        private val airplaneRepository: AirplaneRepository,
        private val directionRepository: DirectionRepository,
        private val cityRepository: CityRepository,
        private val scheduleRepository: ScheduleRepository) {

    init {
        initAirlines()
        initAirplanes()
        initDirections()
        initSchedules()
    }

    private fun initAirlines() {
        airlineRepository.save(Airline("S7", emptyList(), 1))
        airlineRepository.save(Airline("Aeroflot", emptyList(), 2))
        airlineRepository.save(Airline("Russia", emptyList(), 3))
        airlineRepository.save(Airline("Utair", emptyList(), 4))
    }

    private fun initAirplanes() {
        airplaneRepository.save(Airplane("Boeing-777", 100, airlineRepository.getOne(1)))
        airplaneRepository.save(Airplane("Boeing-336", 100, airlineRepository.getOne(1)))
        airplaneRepository.save(Airplane("СУ-26", 2, airlineRepository.getOne(2)))
    }

    private fun initDirections() {
        cityRepository.save(City("Moscow", emptyList(), emptyList(), 1))
        cityRepository.save(City("Salsk", emptyList(), emptyList(), 2))
        cityRepository.save(City("Rostov", emptyList(), emptyList(), 3))
        cityRepository.save(City("Piter", emptyList(), emptyList(), 4))

        directionRepository.save(Direction(cityRepository.getOne(1), cityRepository.getOne(2)))
        directionRepository.save(Direction(cityRepository.getOne(2), cityRepository.getOne(3)))
        directionRepository.save(Direction(cityRepository.getOne(3), cityRepository.getOne(4)))
    }

    private fun initSchedules() {
        scheduleRepository.save(Schedule(
                Calendar.getInstance().let { calendar -> calendar.set(10, 6, 2019, 20, 40); calendar.time },
                Calendar.getInstance().let { calendar -> calendar.set(10, 6, 2019, 20, 40); calendar.time }
        ))
        scheduleRepository.save(Schedule(
                Calendar.getInstance().let { calendar -> calendar.set(10, 6, 2019, 20, 40); calendar.time },
                Calendar.getInstance().let { calendar -> calendar.set(10, 6, 2019, 20, 40); calendar.time }
        ))
        scheduleRepository.save(Schedule(
                Calendar.getInstance().let { calendar -> calendar.set(10, 6, 2019, 20, 40); calendar.time },
                Calendar.getInstance().let { calendar -> calendar.set(10, 6, 2019, 20, 40); calendar.time }
        ))
    }
}