package social.firefly.core.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import social.firefly.core.database.model.DatabaseField

class FieldConverter {
    @TypeConverter
    fun listToJson(value: List<DatabaseField>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(string: String): List<DatabaseField> = Json.decodeFromString(string)
}
