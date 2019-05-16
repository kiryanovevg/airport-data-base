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
        private val scheduleRepository: ScheduleRepository,
        private val flightRepository: FlightRepository,
        private val userRepository: UserRepository,
        private val passengerRepository: PassengerRepository,
        private val ticketRepository: TicketRepository) {

    init {
//        initFunctions()
    }

    fun clear() {
        airlineRepository.deleteAllInBatch()
        airplaneRepository.deleteAllInBatch()
        directionRepository.deleteAllInBatch()
        cityRepository.deleteAllInBatch()
        scheduleRepository.deleteAllInBatch()
        flightRepository.deleteAllInBatch()
        userRepository.deleteAllInBatch()
        ticketRepository.deleteAllInBatch()
        passengerRepository.deleteAllInBatch()
    }

    fun fill() {
        initUsers()
        initAirlines()
        initAirplanes()
        initDirections()
        initSchedules()
        initFlights()
        initPassengers()
        initTickets()
    }

    private fun initUsers() {
        userRepository.save(User("Marina", "111", true))
        userRepository.save(User("Irina", "111", true))
    }

    private fun initAirlines() {
        airlineRepository.save(Airline("S7", emptyList(), 1))
        airlineRepository.save(Airline("Aeroflot", emptyList(), 2))
        airlineRepository.save(Airline("Russia", emptyList(), 3))
        airlineRepository.save(Airline("Utair", emptyList(), 4))
    }

    private fun initAirplanes() {
        airplaneRepository.save(Airplane("Boeing-777", 5, airlineRepository.getOne(1)))
        airplaneRepository.save(Airplane("Boeing-336", 5, airlineRepository.getOne(1)))
        airplaneRepository.save(Airplane("СУ-26", 2, airlineRepository.getOne(2)))
    }

    private fun initDirections() {
        cityRepository.save(City("Moscow", emptyList(), emptyList(), 1))
        cityRepository.save(City("Salsk", emptyList(), emptyList(), 2))
        cityRepository.save(City("Rostov-on-Don", emptyList(), emptyList(), 3))
        cityRepository.save(City("Sankt-Petersburg", emptyList(), emptyList(), 4))

        directionRepository.save(Direction(cityRepository.getOne(1), cityRepository.getOne(2), emptyList()))
        directionRepository.save(Direction(cityRepository.getOne(2), cityRepository.getOne(3), emptyList()))
        directionRepository.save(Direction(cityRepository.getOne(3), cityRepository.getOne(4), emptyList()))
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

    private fun initFlights() {
        flightRepository.save(Flight(
                1000,
                scheduleRepository.getOne(1),
                directionRepository.getOne(1),
                airplaneRepository.getOne(3),
                emptyList()
        ))

        flightRepository.save(Flight(
                2000,
                scheduleRepository.getOne(2),
                directionRepository.getOne(2),
                airplaneRepository.getOne(3),
                emptyList()
        ))

        flightRepository.save(Flight(
                300,
                scheduleRepository.getOne(3),
                directionRepository.getOne(3),
                airplaneRepository.getOne(1),
                emptyList()
        ))
    }

    private fun initPassengers() {
        passengerRepository.save(Passenger(
                "Kiryanov",
                "Evgeniy",
                "Nikolaevich",
                6012, 204611,
                emptyList()
        ))

        passengerRepository.save(Passenger(
                "Huzhahmetov",
                "Ilya",
                "Alekseevich",
                6012, 246888,
                emptyList()
        ))

        passengerRepository.save(Passenger(
                "Blazhko",
                "Alina",
                "Viktorovna",
                6012, 144552,
                emptyList()
        ))

        passengerRepository.save(Passenger(
                "Kiryanova",
                "Marina",
                "Nikolaevna",
                6012, 258369,
                emptyList()
        ))
    }

    private fun initTickets() {
        ticketRepository.save(Ticket(
                true, 1,
                flightRepository.getOne(1),
                passengerRepository.getOne(1)
        ))

        ticketRepository.save(Ticket(
                true, 2,
                flightRepository.getOne(1),
                passengerRepository.getOne(2)
        ))
    }
}