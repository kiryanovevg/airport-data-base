package com.kiryanov.database.controllers

import com.kiryanov.database.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class ExceptionController {

    @ExceptionHandler(value = [(UserService.UserException::class)])
    fun handleUserException(ex: UserService.UserException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(ex.message)
    }
}