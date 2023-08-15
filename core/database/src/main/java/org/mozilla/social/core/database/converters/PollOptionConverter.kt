package org.mozilla.social.core.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mozilla.social.core.database.model.DatabasePollOption

class PollOptionConverter {
    @TypeConverter
    fun listToJson(value: List<DatabasePollOption>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(string: String): List<DatabasePollOption> = Json.decodeFromString(string)
}