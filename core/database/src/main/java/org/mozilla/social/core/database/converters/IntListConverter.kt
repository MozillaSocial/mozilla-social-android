package org.mozilla.social.core.database.converters

import androidx.room.TypeConverter

class IntListConverter {
    @TypeConverter
    fun listToJson(value: List<Int>): String = value.joinToString(separator = ",")

    @TypeConverter
    fun jsonToList(string: String): List<Int> = string.split(",").map { it.toInt() }
}