package org.mozilla.social.core.model

/**
 * Represents a hashtag used within the content of a status.
 */
data class BasicHashTag(
    /**
     * The value of the hashtag after the # sign.
     */
    val name: String,
    /**
     * URL to the hashtag on the instance.
     */
    val url: String,
)
