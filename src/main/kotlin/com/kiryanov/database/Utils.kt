package com.kiryanov.database

import com.kiryanov.database.controllers.RestException

fun HashMap<String, String>.getValueSafety(key: String): String = this[key]
        ?.trim().also { if (it?.isEmpty() == true) throw RestException("Пустое поле $key") }
        ?: throw RestException("$key key doesn't exist")