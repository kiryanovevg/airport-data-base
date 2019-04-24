package com.kiryanov.database.controllers

import com.kiryanov.database.entity.User
import com.kiryanov.database.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/register")
@RestController
class SignUpController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("")
    fun registerUser(@RequestParam("login") login: String?,
                     @RequestParam("password") password: String?
    ): ResponseEntity<User> {
        return if (login?.isNotEmpty() == true && password?.isNotEmpty() == true) {
            ResponseEntity(
                    userService.addUser(login, password),
                    HttpStatus.OK
            )
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }
}