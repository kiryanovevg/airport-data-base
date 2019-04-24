package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.Airline
import com.kiryanov.database.entity.Airplane
import com.kiryanov.database.repositories.AirlineRepository
import com.kiryanov.database.repositories.AirplaneRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AirplaneService {

    @Autowired
    private lateinit var airplaneRepository: AirplaneRepository

    fun getAll(): List<Airplane> = airplaneRepository
            .findAll()

    fun findById(id: Long): Airplane = airplaneRepository
            .findByIdOrNull(id)
            ?: throw RestException("Airplane not found")

    fun addAirplane(dto: Airplane.DTO,
                    airline: Airline
    ): Airplane = airplaneRepository.save(Airplane(dto.model, dto.capacity, airline))

    fun deleteAirplane(id: Long) {
        if (airplaneRepository.existsById(id)) {
            airplaneRepository.deleteById(id)
        } else throw RestException("Not Found")
    }
}