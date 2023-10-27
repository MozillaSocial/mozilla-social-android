package org.mozilla.social.core.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mozilla.social.core.database.model.DatabaseCard

class CardConverter {
    @TypeConverter
    fun listToJson(value: DatabaseCard): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(string: String): DatabaseCard = Json.decodeFromString(string)
}