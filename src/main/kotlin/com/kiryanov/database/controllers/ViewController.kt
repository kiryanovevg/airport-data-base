package com.kiryanov.database.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class ViewController {

    @GetMapping
    fun indexView(model: Model): String {


        model.addAttribute("test", "assad")
        return "index"
    }
}