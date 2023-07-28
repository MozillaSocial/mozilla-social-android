package org.mozilla.social.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mozilla.social.model.entity.paging.Pageable

/**
 * Represents a hashtag used within the content of a status.
 */
@Serializable
data class HashTag(

    /**
     * The value of the hashtag after the # sign.
     */
    @SerialName("name")
    val name: String,

    /**
     * URL to the hashtag on the instance.
     */
    @SerialName("url")
    val url: String,

    /**
     * Hashtag usage statistics for given days.
     */
    @SerialName("history")
    val history: List<History>? = null
) : Pageable {

    override val id: String
        get() = name
}
