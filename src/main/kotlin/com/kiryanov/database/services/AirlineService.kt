package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.Airline
import com.kiryanov.database.getValueSafety
import com.kiryanov.database.repositories.AirlineRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AirlineService {

    @Autowired
    private lateinit var airlineRepository: AirlineRepository

    fun getAll(): List<Airline> = airlineRepository
            .findAll()

    fun findById(id: Long): Airline = airlineRepository
            .findByIdOrNull(id)
            ?: throw RestException("Airline not found")

    fun addAirline(dto: HashMap<String, String>?): Airline = dto?.let { map ->
        val name = map.getValueSafety("name")

        val airline = Airline(name, emptyList())
        return airlineRepository.save(airline)
    } ?: throw RestException("Empty RequestBody")

    fun deleteAirline(id: Long) {
        if (airlineRepository.existsById(id)) {
            airlineRepository.deleteById(id)
        } else throw RestException("Not Found")
    }
}