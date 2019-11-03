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
        ticketRepository.deleteAll()
        passengerRepository.deleteAll()
        userRepository.deleteAll()
        flightRepository.deleteAll()
        scheduleRepository.deleteAll()
        cityRepository.deleteAll()
        directionRepository.deleteAll()
        airplaneRepository.deleteAll()
        airlineRepository.deleteAll()
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
        userRepository.save(User("Marina", "111", User.Role.ADMIN))
        userRepository.save(User("Irina", "111", User.Role.ADMIN))
        userRepository.save(User("admin", "+", User.Role.ADMIN))
        userRepository.save(User("cashier", "+", User.Role.CASHIER))
        userRepository.save(User("dispatcher", "+", User.Role.DISPATCHER))
        userRepository.save(User("security", "+", User.Role.SECURITY))
    }

    private fun initAirlines() {
        airlineRepository.save(Airline("S7", emptyList()))
        airlineRepository.save(Airline("Aeroflot", emptyList()))
        airlineRepository.save(Airline("Russia", emptyList()))
        airlineRepository.save(Airline("Utair", emptyList()))
    }

    private fun initAirplanes() {
        val airlines = airlineRepository.findAll()

        airplaneRepository.save(Airplane("Boeing-777", 5, airlines[0]))
        airplaneRepository.save(Airplane("Boeing-336", 5, airlines[1]))
        airplaneRepository.save(Airplane("СУ-26", 2, airlines[2]))
    }

    private fun initDirections() {
        cityRepository.save(City("Moscow", emptyList(), emptyList()))
        cityRepository.save(City("Salsk", emptyList(), emptyList()))
        cityRepository.save(City("Rostov-on-Don", emptyList(), emptyList()))
        cityRepository.save(City("Sankt-Petersburg", emptyList(), emptyList()))

        val cities = cityRepository.findAll()
        directionRepository.save(Direction(cities[0], cities[2]))
        directionRepository.save(Direction(cities[1], cities[3]))
        directionRepository.save(Direction(cities[3], cities[2]))
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
                scheduleRepository.findAll()[0],
                directionRepository.findAll()[1],
                airplaneRepository.findAll()[2],
                emptyList()
        ))

        flightRepository.save(Flight(
                2000,
                scheduleRepository.findAll()[2],
                directionRepository.findAll()[2],
                airplaneRepository.findAll()[1],
                emptyList()
        ))

        flightRepository.save(Flight(
                300,
                scheduleRepository.findAll()[0],
                directionRepository.findAll()[1],
                airplaneRepository.findAll()[0],
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
                111, 1,
                flightRepository.findAll()[0],
                passengerRepository.findAll()[0]
        ))

        ticketRepository.save(Ticket(
                222, 2,
                flightRepository.findAll()[0],
                passengerRepository.findAll()[1]
        ))
    }
}