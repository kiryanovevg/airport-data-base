package com.kiryanov.database.controllers

import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.util.*
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/api/db")
class DataBaseController {

    companion object {
        private const val BACKUP = "backup"
        private const val RESTORE = "restore"
    }

    private val host = "ec2-54-247-70-127.eu-west-1.compute.amazonaws.com"
    private val user = "uqpjfgcmmyytyv"
    private val database = "dbektvt94eq8il"
    private val password = "39013be08b25bb41026d92a39398a4eb94bb0276348e1522e79229cbd94b5586"
    private val backupFilePath = File(System.getProperty("user.home") + File.separator + "backupAirportDB").absolutePath

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
    fun backup(response: HttpServletResponse): ResponseEntity<String> {
        val result = execute(host, user, database, password, BACKUP, backupFilePath)

//        response.contentType = "application/sql"
        response.contentType = "application/octet-stream"
        response.setHeader("Content-disposition", "attachment; filename=${result.first.name}")
        IOUtils.copy(result.first.inputStream(), response.outputStream)
        return ResponseEntity.ok(result.second)
    }

    @GetMapping("/restore")
    fun getRestore(): ResponseEntity<String> {
        val page = "<html>\n" +
                "\n" +
                "<body>\n" +
                "<h1>Upload backup file</h1>\n" +
                "\n" +
                "<form method=\"POST\" action=\"/api/db/restore\" enctype=\"multipart/form-data\">\n" +
                "    <input type=\"file\" name=\"file\" /><br/>\n" +
                "    <input type=\"submit\" value=\"Submit\" />\n" +
                "</form>\n" +
                "\n" +
                "</body>\n" +
                "</html>"

        return ResponseEntity.ok(page)
    }

    @PostMapping("/restore")
    fun postRestore(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        val fileName = "uploadedTempFile"
        val tempFile = File(fileName)
        tempFile.deleteOnExit()
        tempFile.createNewFile()
        IOUtils.copy(file.inputStream, tempFile.outputStream())

        val result = execute(host, user, database, password, RESTORE, tempFile.absolutePath)

        return ResponseEntity.ok(result.second)
    }

    private fun execute(host: String, user: String,
                        databaseName: String, databasePassword: String,
                        type: String, filePath: String): Pair<File, String> {

        val commands = getPgCommands(host, user, databaseName, filePath, type)
        return if (commands.isNotEmpty()) {
            try {
                val pb = ProcessBuilder(commands)
                pb.environment()["PGPASSWORD"] = databasePassword

                val process = pb.start()

                val builder = StringBuilder()
                BufferedReader(
                        InputStreamReader(process.errorStream)).use { buf ->
                    var line: String? = buf.readLine()
                    while (line != null) {
                        System.err.println(line)
                        builder.append(line).append("\n")
                        line = buf.readLine()
                    }
                }

                process.waitFor()
                process.destroy()

                println("===> Success on $type process.")
                Pair(File(filePath), builder.toString())
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
                              databaseName: String, backupFilePath: String,
                              type: String): List<String> {

        val commands = ArrayList<String>()
        when (type) {
            BACKUP -> {
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
                commands.add(backupFilePath)
                commands.add("-d") //database name
                commands.add(databaseName)
            }
            RESTORE -> {
                commands.add("pg_restore")
                commands.add("-h")
                commands.add(host)
                commands.add("-p")
                commands.add("5432")
                commands.add("-U")
                commands.add(user)
                commands.add("-d")
                commands.add(databaseName)
                commands.add("-v")
                commands.add(backupFilePath)
            }
            else -> return ArrayList()
        }
        return commands
    }
}