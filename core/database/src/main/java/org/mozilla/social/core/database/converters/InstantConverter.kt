package org.mozilla.social.core.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantConverter {
    @TypeConverter
    fun Long?.toInstant(): Instant? = this?.let { Instant.fromEpochMilliseconds(it) }

    @TypeConverter
    fun Instant?.toLong(): Long? = this?.toEpochMilliseconds()
}