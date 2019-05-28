package com.kiryanov.database.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
@RequestMapping("/api/db")
class DataBaseController {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    private val jBackupController = JBackupController()

    private val host = "ec2-54-247-70-127.eu-west-1.compute.amazonaws.com"
    private val user = "uqpjfgcmmyytyv"
    private val database = "dbektvt94eq8il"
    private val password = "39013be08b25bb41026d92a39398a4eb94bb0276348e1522e79229cbd94b5586"

    @GetMapping("/backup")
    fun backup(): File {
        return jBackupController.executeCommand(host, user, database, password,"backup")
    }

    @GetMapping("/restore")
    fun restore(): String {
        jBackupController.executeCommand(host, user, database, password,"restore")

        return "restore"
    }
}