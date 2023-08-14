package org.mozilla.social.core.database.model

/**
 * Represents a hashtag used within the content of a status.
 */
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
    val history: List<DatabaseHistory>? = null
)
