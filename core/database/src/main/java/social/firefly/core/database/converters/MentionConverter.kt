package social.firefly.core.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import social.firefly.core.database.model.DatabaseMention

class MentionConverter {
    @TypeConverter
    fun listToJson(value: List<DatabaseMention>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(string: String): List<DatabaseMention> = Json.decodeFromString(string)
}
