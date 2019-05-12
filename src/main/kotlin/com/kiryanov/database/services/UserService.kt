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

    fun addUser(login: String, password: String, adminPermission: Boolean) = userRepository
            .save(User(login, password, adminPermission))

    fun getUser(user: User?): User {
        if (user != null) {
            if (user.login.isNotEmpty() && user.password.isNotEmpty()) {
                val findUser = userRepository.findAll().find { user.login == it.login }
//                val findUser = userRepository.findByLogin(user.login)
                if (findUser != null) {
                    if (findUser.password == user.password) {
                        return findUser
                    } else throw RestException("Неверный пароль")
                } else throw RestException("Пользователь не найден")
            } else throw RestException("Введите данные")
        } else throw RestException("Введите данные")
    }
}