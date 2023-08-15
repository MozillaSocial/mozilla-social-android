package org.mozilla.social.core.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

class LocalDateConverter {
    @TypeConverter
    fun localDateToSting(localDate: LocalDate): String = localDate.toString()

    @TypeConverter
    fun stringToLocalDate(string: String): LocalDate = string.toLocalDate()
}