package com.kiryanov.database.controllers

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.net.InetAddress

@Controller
@RequestMapping("/")
class ViewController {

    @Value("\${dev}")
    private var isDevMode = false

    @GetMapping
    fun indexView(model: Model): String {
        model.addAttribute("isDevMode", isDevMode)
        if (isDevMode) model.addAttribute("host", InetAddress.getLocalHost().hostName)
        return "index"
    }
}