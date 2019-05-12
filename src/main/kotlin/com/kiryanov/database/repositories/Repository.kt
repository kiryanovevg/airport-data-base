package com.kiryanov.database.repositories

import com.kiryanov.database.entity.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AirlineRepository : JpaRepository<Airline, Long>
interface AirplaneRepository : JpaRepository<Airplane, Long>
interface DirectionRepository : JpaRepository<Direction, Long>
interface CityRepository : JpaRepository<City, Long>
interface FlightRepository : JpaRepository<Flight, Long>
interface PassengerRepository : JpaRepository<Passenger, Long>
interface ScheduleRepository : JpaRepository<Schedule, Long>
interface TicketRepository : JpaRepository<Ticket, Long>
interface UserRepository : JpaRepository<User, Long> {

//    @Query(value = "SELECT u from User u where u.login = :login")
//    fun findByLogin(@Param("login") login: String): User?
}