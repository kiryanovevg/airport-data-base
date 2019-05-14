package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.City
import com.kiryanov.database.entity.Direction
import com.kiryanov.database.entity.Flight
import com.kiryanov.database.getValueSafety
import com.kiryanov.database.repositories.CityRepository
import com.kiryanov.database.repositories.DirectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DirectionService {

    @Autowired
    private lateinit var directionRepository: DirectionRepository

    @Autowired
    private lateinit var cityRepository: CityRepository

    fun getAllCities(): List<City> = cityRepository
            .findAll()

    fun getAllDirections(): List<Direction> = directionRepository
            .findAll()

    fun addCity(dto: HashMap<String, String>?): City = dto?.let { map ->
        val name = map.getValueSafety("name")
        val city = City(name, emptyList(), emptyList())
        return cityRepository.save(city)
    } ?: throw RestException("Empty RequestBody")

    fun deleteCity(id: Long) {
        if (cityRepository.existsById(id)) {
            cityRepository.deleteById(id)
        } else throw RestException("Not Found")
    }

    fun findCityById(id: Long): City = cityRepository
            .findByIdOrNull(id)
            ?: throw RestException("City not found")

    fun addDirection(dto: HashMap<String, String>?): Direction = dto?.let { map ->
        val fromCity = findCityById(map.getValueSafety("fromCityId").toLong())
        val toCity = findCityById(map.getValueSafety("toCityId").toLong())

        if (fromCity.id == toCity.id) throw RestException("Неверное направление")
        if (fromCity.fromDirections.firstOrNull { it.toCity.id == toCity.id } != null)
            throw RestException("Direction already exist")


        return directionRepository.save(Direction(fromCity, toCity, emptyList()))
    } ?: throw RestException("Empty RequestBody")

    fun deleteDirection(id: Long) {
        if (directionRepository.existsById(id)) {
            directionRepository.deleteById(id)
        } else throw RestException("Not Found")
    }

    fun findById(id: Long): Direction = directionRepository
            .findByIdOrNull(id)
            ?: throw RestException("Direction not found")
}