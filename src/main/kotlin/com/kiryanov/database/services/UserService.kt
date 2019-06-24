package com.kiryanov.database.services

import com.kiryanov.database.controllers.RestException
import com.kiryanov.database.entity.User
import com.kiryanov.database.entity.UserChanges
import com.kiryanov.database.getValueSafety
import com.kiryanov.database.repositories.UserChangesRepository
import com.kiryanov.database.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userChangesRepository: UserChangesRepository

    fun getAll(): List<User> = userRepository.findAll()

    fun getRoleList(): List<String> = listOf(
            User.Role.ADMIN, User.Role.CASHIER, User.Role.DISPATCHER, User.Role.SECURITY
    )

    fun addUser(dto: HashMap<String, String>?): User = dto?.let { map ->
        val login = map.getValueSafety("login")
        val password = map.getValueSafety("password")
        val userRole = map.getValueSafety("userRole")

        if (userRepository.findAll().firstOrNull { it.login == login } != null)
            throw RestException("Пользователь с таким именем уже существует")

        val id = if (userRepository.count() != 0.toLong()) userRepository.findAll().last().id + 1 else 1

        return userRepository.save(User(login, password, userRole, id))
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

    fun backupLastAction(): String = if (userChangesRepository.count() == 0.toLong())
        throw RestException("Actions list is empty")
    else {
        val changes = userChangesRepository.findAll().last()
        when {
            changes.action == UserChanges.Action.INSERT -> {
                delete(changes.userId)
                "User(id - ${changes.userId}) deleted"
            }
            changes.action == UserChanges.Action.DELETE -> {
                userRepository.save(changes.getUser())
                "${changes.getUser()} - restored"
            }
            else -> throw RestException("Error on backup last action")
        }.also {
            userChangesRepository.delete(changes)
            userChangesRepository.delete(userChangesRepository.findAll().last())
        }
    }
}