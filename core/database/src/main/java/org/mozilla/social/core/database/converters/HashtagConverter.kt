package org.mozilla.social.core.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mozilla.social.core.database.model.DatabaseHashTag

class HashtagConverter {
    @TypeConverter
    fun listToJson(value: List<DatabaseHashTag>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(string: String): List<DatabaseHashTag> = Json.decodeFromString(string)
}
