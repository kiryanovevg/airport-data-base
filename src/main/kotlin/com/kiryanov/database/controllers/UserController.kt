package com.kiryanov.database.controllers

import com.kiryanov.database.DataComponent
import com.kiryanov.database.entity.User
import com.kiryanov.database.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest

@RequestMapping("/api")
@RestController
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var dataComponent: DataComponent

    @GetMapping("/clear")
    fun clearData(): ResponseEntity<String> {
        dataComponent.clear()
        return ResponseEntity.ok("Data deleted </br> <a href=\"/api/fill\">Fill</a>")
    }

    @GetMapping("/fill")
    fun fillData(): ResponseEntity<String> {
        dataComponent.fill()
        return ResponseEntity.ok("Data added")
    }

    @GetMapping("/user/register")
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

    @PostMapping("/user/login")
    fun loginUser(@RequestBody user: User?): User = userService.getUser(user)
}