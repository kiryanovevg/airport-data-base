package com.kiryanov.database.controllers

import com.fasterxml.jackson.annotation.JsonView
import com.kiryanov.database.entity.Flight
import com.kiryanov.database.services.FlightService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJacksonValue
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/flights")
class FlightController {

    @Autowired
    private lateinit var flightService: FlightService

    private interface Rest

    @JsonView(Rest::class)
    @GetMapping
    fun getAllFlight(
            @RequestParam(value = "free_places", defaultValue = "false") freePlaces: Boolean,
            @RequestParam(value = "full_data", defaultValue = "false") fullData: Boolean
    ): MappingJacksonValue {
        val result = if (freePlaces) {
            flightService.getFlightWithEmptyPlaces()
        } else {
            flightService.getAll()
        }

        return MappingJacksonValue(result).apply {
//            serializationView = if (fullData) View.Airline
        }
    }

    @JsonView(Rest::class)
    @GetMapping("/{id}/places")
    fun getFreePlaces(@PathVariable("id") id: Long): List<Int> {
        return flightService.getFreePlaces(id)
    }

    @JsonView(Rest::class)
    @PostMapping
    fun addFlight(@RequestBody dto: HashMap<String, String>?): Flight {
        return flightService.addFlight(dto)
    }

    @DeleteMapping("/{id}")
    fun deleteFlight(@PathVariable("id") id: Long): ResponseEntity<String> {
        flightService.deleteFlight(id)
        return ResponseEntity.ok("Successful deleted")
    }
}