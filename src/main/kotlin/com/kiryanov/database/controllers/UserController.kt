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
                     @RequestParam("password") password: String?,
                     @RequestParam("admin") adminPermission: Boolean?
    ): ResponseEntity<User> {
        return if (login?.isNotEmpty() == true
                && password?.isNotEmpty() == true
                && adminPermission?.toString()?.isNotEmpty() == true) {
            ResponseEntity(
                    userService.addUser(login, password, adminPermission),
                    HttpStatus.OK
            )
        } else throw RestException("Введите все данные")
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody user: User?): User = userService.getUser(user)
}