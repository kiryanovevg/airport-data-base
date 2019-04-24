package com.kiryanov.database.services

import com.kiryanov.database.repositories.AirlineRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AirlineService {

    @Autowired
    private lateinit var airlineRepository: AirlineRepository
}