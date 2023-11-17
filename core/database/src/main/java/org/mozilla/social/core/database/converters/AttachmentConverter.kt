package org.mozilla.social.core.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mozilla.social.core.database.model.DatabaseAttachment

class AttachmentConverter {
    @TypeConverter
    fun listToJson(value: List<DatabaseAttachment>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(string: String): List<DatabaseAttachment> = Json.decodeFromString(string)
}
