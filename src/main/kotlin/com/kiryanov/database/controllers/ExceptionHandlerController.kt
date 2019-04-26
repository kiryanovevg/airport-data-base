package com.kiryanov.database.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class ExceptionHandlerController {

    @ExceptionHandler(value = [(RestException::class)])
    fun handleRestException(e: RestException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }

    /*@ExceptionHandler(value = [(AirlineService.AirlineException::class)])
    fun handleUserException(e: AirlineService.AirlineException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }*/
}

class RestException(override val message: String?) : Exception()