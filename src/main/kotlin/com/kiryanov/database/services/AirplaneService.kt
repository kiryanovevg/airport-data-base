package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.Airplane
import com.kiryanov.database.getValueSafety
import com.kiryanov.database.repositories.AirplaneRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AirplaneService {

    @Autowired
    private lateinit var airplaneRepository: AirplaneRepository

    @Autowired
    private lateinit var airlineService: AirlineService

    fun getAll(): List<Airplane> = airplaneRepository
            .findAll()

    fun findById(id: Long): Airplane = airplaneRepository
            .findByIdOrNull(id)
            ?: throw RestException("Airplane not found")

    fun addAirplane(dto: HashMap<String, String>?): Airplane = dto?.let { map ->
        val model = map.getValueSafety("model")
        val capacity = map.getValueSafety("capacity").toInt()
        val airlineId = map.getValueSafety("airlineId").toLong()

        val airline = airlineService.findById(airlineId)
        return airplaneRepository.save(Airplane(model, capacity, airline))
    } ?: throw RestException("Empty RequestBody")

    fun deleteAirplane(id: Long) {
        if (airplaneRepository.existsById(id)) {
            airplaneRepository.deleteById(id)
        } else throw RestException("Not Found")
    }
}