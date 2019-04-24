package com.kiryanov.database.services

import com.kiryanov.database.entity.User
import com.kiryanov.database.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.lang.Exception

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
                    throw UserException("Неверный пароль")
                }
            } else {
                throw UserException("Пользователь не найден")
            }
        } else {
            throw UserException("Введите данные")
        }
    }

    class UserException(override val message: String?) : Exception()
}