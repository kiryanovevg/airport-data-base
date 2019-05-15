package com.kiryanov.database

import com.kiryanov.database.controllers.RestException
import java.text.SimpleDateFormat
import java.util.*

fun HashMap<String, String>.getValueSafety(key: String): String = this[key]
        ?.trim().also { if (it?.isEmpty() == true) throw RestException("Пустое поле $key") }
        ?: throw RestException("$key key doesn't exist")

object DateParser {

    fun parse(input: String): Date {
        var result = input

        //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        //things a bit.  Before we go on we have to repair this.
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz")

        //this is zero time so we need to add that TZ indicator for
        result = if (result.endsWith("Z")) {
            result.substring(0, result.length - 1) + "GMT-00:00"
        } else {
            val inset = 6

            val s0 = result.substring(0, result.length - inset)
            val s1 = result.substring(result.length - inset, result.length)

            s0 + "GMT" + s1
        }

        return df.parse(result)

    }

    fun toString(date: Date): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
        val tz = TimeZone.getTimeZone("UTC")
        df.timeZone = tz
        val output = df.format(date)
        val inset0 = 9
        val inset1 = 6
        val s0 = output.substring(0, output.length - inset0)
        val s1 = output.substring(output.length - inset1, output.length)
        var result = s0 + s1
        result = result.replace("UTC".toRegex(), "+00:00")
        return result

    }

}