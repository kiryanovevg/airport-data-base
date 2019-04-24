package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.User
import com.kiryanov.database.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun findAll(): List<User> = userRepository.findAll()

    fun addUser(login: String, password: String) = userRepository
            .save(User(login, password, false))

    fun getUser(login: String?, password: String?): User {

        if (login?.isNotEmpty() == true && password?.isNotEmpty() == true) {
            val user = userRepository.findByLogin(login)

            if (user != null) {
                if (user.password == password) {
                     return user
                } else {
                    throw RestException("Неверный пароль")
                }
            } else {
                throw RestException("Пользователь не найден")
            }
        } else {
            throw RestException("Введите данные")
        }
    }
}