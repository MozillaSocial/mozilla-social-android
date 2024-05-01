package social.firefly.core.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import social.firefly.core.database.model.DatabaseAttachment

class AttachmentConverter {
    @TypeConverter
    fun listToJson(value: List<DatabaseAttachment>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(string: String): List<DatabaseAttachment> = Json.decodeFromString(string)
}
