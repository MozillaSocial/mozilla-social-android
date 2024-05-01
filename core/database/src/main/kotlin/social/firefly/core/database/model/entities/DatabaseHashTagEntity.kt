package social.firefly.core.database.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import social.firefly.core.database.model.DatabaseHistory

@Entity(tableName = "hashtags")
data class DatabaseHashTagEntity(
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
     * If the user is following the hashtag
     */
    val following: Boolean,
    /**
     * Hashtag usage statistics for given days.
     */
    val history: List<DatabaseHistory>? = null,
)