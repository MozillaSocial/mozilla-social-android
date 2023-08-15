package org.mozilla.social.core.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a hashtag used within the content of a status.
 */
@Entity(tableName = "hashTags")
data class DatabaseHashTag(

    /**
     * The value of the hashtag after the # sign.
     */
    @PrimaryKey
    val name: String,

    /**
     * URL to the hashtag on the instance.
     */
    val url: String,

    /**
     * Hashtag usage statistics for given days.
     */
    @Embedded(prefix = "history_")
    val history: List<DatabaseHistory>? = null
)
