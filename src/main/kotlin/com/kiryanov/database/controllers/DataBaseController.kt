package com.kiryanov.database.controllers

import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/api/db")
class DataBaseController {

    private val host = "ec2-54-247-70-127.eu-west-1.compute.amazonaws.com"
    private val user = "uqpjfgcmmyytyv"
    private val database = "dbektvt94eq8il"
    private val password = "39013be08b25bb41026d92a39398a4eb94bb0276348e1522e79229cbd94b5586"

    /*@GetMapping("/test")
    @ResponseBody
    fun test(response: HttpServletResponse) {
        val file = File(System.getProperty("user.home")
                + File.separator + "app-debug.apk")

        response.contentType = "application/apk"
        response.setHeader("Content-disposition", "attachment; filename=${file.name}")
        IOUtils.copy(file.inputStream(), response.outputStream)
    }*/

    @GetMapping("/backup")
    fun backup(response: HttpServletResponse) {
        val file = execute(host, user, database, password,"backup")

        response.contentType = "application/sql"
        response.setHeader("Content-disposition", "attachment; filename=${file.name}")
        IOUtils.copy(file.inputStream(), response.outputStream)
    }

    @GetMapping("/restore")
    fun restore(): String {
        execute(host, user, database, password,"restore")

        return "restore"
    }

    private fun execute(host: String, user: String,
                        databaseName: String, databasePassword: String,
                        type: String): File {
        val backupFilePath = File(System.getProperty("user.home")
                + File.separator + "backup_" + databaseName)

        if (!backupFilePath.exists()) {
            backupFilePath.mkdirs()
        }

        val sdf = SimpleDateFormat("yyyyMMdd")
//        String backupFileName = "backup_" + databaseName + "_" + sdf.format(new Date()) + ".sql";
        val backupFileName = "backup_" + databaseName + "_" + "1" + ".sql"

        val commands = getPgCommands(host, user, databaseName, backupFilePath, backupFileName, type)
        return if (commands.isNotEmpty()) {
            try {
                val pb = ProcessBuilder(commands)
                pb.environment()["PGPASSWORD"] = databasePassword

                val process = pb.start()

                BufferedReader(
                        InputStreamReader(process.errorStream)).use { buf ->
                    var line: String? = buf.readLine()
                    while (line != null) {
                        System.err.println(line)
                        line = buf.readLine()
                    }
                }

                process.waitFor()
                process.destroy()

                println("===> Success on $type process.")
                File(backupFilePath.absolutePath + File.separator + backupFileName)
            } catch (ex: IOException) {
                println("Exception: $ex")
                throw RestException(ex.message)
            } catch (ex: InterruptedException) {
                println("Exception: $ex")
                throw RestException(ex.message)
            } catch (ex: Exception) {
                throw RestException(ex.message)
            }
        } else {
            throw RestException("Error: Invalid params.")
        }
    }

    private fun getPgCommands(host: String, user: String,
                              databaseName: String, backupFilePath: File,
                              backupFileName: String, type: String): List<String> {

        val commands = ArrayList<String>()
        when (type) {
            "backup" -> {
                commands.add("pg_dump")
                commands.add("-h") //database server host
                commands.add(host)
                commands.add("-p") //database server port number
                commands.add("5432")
                commands.add("-U") //connect as specified database user
                commands.add(user)
                commands.add("-F") //output file format (custom, directory, tar, plain text (default))
                commands.add("c")
                commands.add("-b") //include large objects in dump
                commands.add("-v") //verbose mode
                commands.add("-f") //output file or directory name
                commands.add(backupFilePath.absolutePath + File.separator + backupFileName)
                commands.add("-d") //database name
                commands.add(databaseName)
            }
            "restore" -> {
                commands.add("pg_restore")
                commands.add("-h")
                commands.add("localhost")
                commands.add("-p")
                commands.add("5432")
                commands.add("-U")
                commands.add("postgres")
                commands.add("-d")
                commands.add(databaseName)
                commands.add("-v")
                commands.add(backupFilePath.absolutePath + File.separator + backupFileName)
            }
            else -> return ArrayList()
        }
        return commands
    }
}