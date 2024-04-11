package social.firefly.core.database.model

import kotlinx.serialization.Serializable

/**
 * Represents a hashtag used within the content of a status.
 */
@Serializable
data class DatabaseHashTag(
    /**
     * The value of the hashtag after the # sign.
     */
    val name: String,
    /**
     * URL to the hashtag on the instance.
     */
    val url: String,
    /**
     * Hashtag usage statistics for given days.
     */
    val history: List<DatabaseHistory>? = null,
)
