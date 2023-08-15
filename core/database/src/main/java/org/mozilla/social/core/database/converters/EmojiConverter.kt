package org.mozilla.social.core.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mozilla.social.core.database.model.DatabaseEmoji

class EmojiConverter {
    @TypeConverter
    fun listToJson(value: List<DatabaseEmoji>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(string: String): List<DatabaseEmoji> = Json.decodeFromString(string)
}