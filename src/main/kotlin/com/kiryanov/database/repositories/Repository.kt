package com.kiryanov.database.repositories

import com.kiryanov.database.entity.Airline
import org.springframework.data.jpa.repository.JpaRepository

interface AirlineRepository : JpaRepository<Airline, Long> {
}