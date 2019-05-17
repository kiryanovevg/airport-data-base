package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.User
import com.kiryanov.database.getValueSafety
import com.kiryanov.database.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun getAll(): List<User> = userRepository.findAll()

    fun getRoleList(): List<String> = listOf(
            User.Role.ADMIN, User.Role.CASHIER, User.Role.DISPATCHER, User.Role.SECURITY
    )

    fun addUser(dto: HashMap<String, String>?): User = dto?.let { map ->
        val login = map.getValueSafety("login")
        val password = map.getValueSafety("password")
        val role = map.getValueSafety("role")

        if (userRepository.findAll().firstOrNull { it.login == login } != null)
            throw RestException("Пользователь с таким именем уже существует")

        return userRepository.save(User(login, password, role))
    } ?: throw RestException("Empty RequestBody")

    fun getUser(dto: HashMap<String, String>?): User = dto?.let { map ->
        val login = map.getValueSafety("login")
        val password = map.getValueSafety("password")

        return userRepository
                .findAll()
                .firstOrNull { it.login == login }
                ?.let { user ->
                    if (user.password != password) throw RestException("Неверный пароль")
                    else user
                } ?: throw RestException("Пользователь не найден")
    } ?: throw RestException("Empty RequestBody")

    fun delete(id: Long) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
        } else throw RestException("Not Found")
    }
}