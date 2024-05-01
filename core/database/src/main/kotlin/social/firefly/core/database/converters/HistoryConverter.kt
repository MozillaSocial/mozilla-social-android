package social.firefly.core.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import social.firefly.core.database.model.DatabaseHistory

class HistoryConverter {
    @TypeConverter
    fun listToJson(value: List<DatabaseHistory>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(string: String): List<DatabaseHistory> = Json.decodeFromString(string)
}
