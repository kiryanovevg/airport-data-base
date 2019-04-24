package com.kiryanov.database.controllers

import com.kiryanov.database.entity.User
import com.kiryanov.database.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest

@RequestMapping("/user")
@RestController
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/register")
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

    @GetMapping("/login")
    fun loginUser(@RequestParam("login") login: String?,
                  @RequestParam("password") password: String?
    ): User = userService.getUser(login, password)
}