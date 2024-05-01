package social.firefly.core.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import social.firefly.core.database.model.DatabaseEmoji

class EmojiConverter {
    @TypeConverter
    fun listToJson(value: List<DatabaseEmoji>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(string: String): List<DatabaseEmoji> = Json.decodeFromString(string)
}
