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

    fun addUser(dto: HashMap<String, String>?): User = dto?.let { map ->
        val login = map.getValueSafety("login")
        val password = map.getValueSafety("password")

        if (userRepository.findAll().firstOrNull { it.login == login } != null)
            throw RestException("Пользователь с таким именем уже существует")

        return userRepository.save(User(login, password, true))
    } ?: throw RestException("Empty RequestBody")

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

    fun delete(id: Long) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
        } else throw RestException("Not Found")
    }
}