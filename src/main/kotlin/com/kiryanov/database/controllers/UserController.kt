package com.kiryanov.database.controllers

import com.kiryanov.database.entity.User
import com.kiryanov.database.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/user")
@RestController
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping
    fun getAllUsers(): List<User> {
        return userService.getAll()
    }

    @GetMapping("/roles")
    fun getUserRoles(): List<String> {
        return userService.getRoleList()
    }

    @PostMapping
    fun addUser(@RequestBody dto: HashMap<String, String>?): User {
        return userService.addUser(dto)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable("id") id: Long): ResponseEntity<String> {
        userService.delete(id)
        return ResponseEntity.ok("Successful deleted")
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody dto: HashMap<String, String>?): User = userService.getUser(dto)
}