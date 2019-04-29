package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.Airline
import com.kiryanov.database.entity.Airplane
import com.kiryanov.database.entity.City
import com.kiryanov.database.entity.Direction
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

    fun addCity(dto: City.DTO?): City {
        if (dto != null) {
            if (dto.name.trim().isNotEmpty()) {
                val city = City(dto.name.trim(), emptyList(), emptyList())
                return cityRepository.save(city)
            } else throw RestException("Пустые поля")
        } else throw RestException("Введите данные")
    }

    fun deleteCity(id: Long) {
        if (cityRepository.existsById(id)) {
            cityRepository.deleteById(id)
        } else throw RestException("Not Found")
    }

    fun findCityById(id: Long): City = cityRepository
            .findByIdOrNull(id)
            ?: throw RestException("City not found")

    fun addDirection(fromCity: City, toCity: City): Direction {
        if (fromCity.id != toCity.id) {
            return directionRepository.save(
                    Direction(fromCity, toCity, emptyList())
            )
        } else throw RestException("Неверное направление")
    }

    fun deleteDirection(id: Long) {
        if (directionRepository.existsById(id)) {
            directionRepository.deleteById(id)
        } else throw RestException("Not Found")
    }
}