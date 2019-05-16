package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.Flight
import com.kiryanov.database.entity.Passenger
import com.kiryanov.database.entity.Schedule
import com.kiryanov.database.entity.Ticket
import com.kiryanov.database.getValueSafety
import com.kiryanov.database.repositories.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PassengerService {

    @Autowired
    private lateinit var passengerRepository: PassengerRepository

    fun getAll(): List<Passenger> = passengerRepository
            .findAll()

    fun findById(id: Long): Passenger = passengerRepository
            .findByIdOrNull(id)
            ?: throw RestException("Ticket not found")

    fun add(dto: HashMap<String, String>?): Passenger = dto?.let { map ->
        val surname = map.getValueSafety("surname")
        val name = map.getValueSafety("name")
        val patronymic = map.getValueSafety("patronymic")
        val series = map.getValueSafety("series").toInt()
        val number = map.getValueSafety("number").toInt()

        if (passengerRepository.findAll().firstOrNull { it.series == series && it.number == number } != null)
            throw RestException("Паспортные данные уже внесены!")

        return passengerRepository.save(Passenger(surname, name, patronymic, series, number))
    } ?: throw RestException("Empty RequestBody")

    fun delete(id: Long) {
        if (passengerRepository.existsById(id)) {
            passengerRepository.deleteById(id)
        } else throw RestException("Not Found")
    }
}