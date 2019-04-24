package com.kiryanov.database.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class userService {

    @Autowired
    private lateinit var directionService: userService
}