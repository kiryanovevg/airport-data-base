package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.Airline
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

    fun addAirline(dto: Airline.DTO?): Airline {
        if (dto != null) {
            if (dto.name.trim().isNotEmpty()) {
                val airline = Airline(dto.name.trim(), emptyList())
                return airlineRepository.save(airline)
            } else throw RestException("Пустые поля")
        } else throw RestException("Введите данные")
    }

    fun deleteAirline(id: Long) {
        if (airlineRepository.existsById(id)) {
            airlineRepository.deleteById(id)
        } else throw RestException("Not Found")
    }
}