package org.mozilla.social.core.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mozilla.social.core.database.model.DatabaseHistory

class HistoryConverter {
    @TypeConverter
    fun listToJson(value: List<DatabaseHistory>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(string: String): List<DatabaseHistory> = Json.decodeFromString(string)
}