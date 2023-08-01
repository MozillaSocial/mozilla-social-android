package org.mozilla.social.model

import org.mozilla.social.model.paging.Pageable

/**
 * Represents a hashtag used within the content of a status.
 */
data class HashTag(

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
    val history: List<History>? = null
) : Pageable {

    override val id: String
        get() = name
}
