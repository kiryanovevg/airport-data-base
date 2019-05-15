package com.kiryanov.database.controllers

import com.kiryanov.database.DataComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class MainController {

    @Autowired
    private lateinit var dataComponent: DataComponent

    @GetMapping
    fun main(): String {
        return "<a href=\"/api/fill\">Fill</a><br><a href=\"/api/clear\">Clear</a>"
    }

    @GetMapping("/clear")
    fun clearData(): String {
        dataComponent.clear()
        return "Data deleted </br> <a href=\"/api/fill\">Fill</a>"
    }

    @GetMapping("/fill")
    fun fillData(): String {
        dataComponent.fill()
        return "Data added </br> <a href=\"/api\">Go Back</a>"
    }
}