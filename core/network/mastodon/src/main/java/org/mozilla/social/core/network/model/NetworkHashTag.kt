package org.mozilla.social.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mozilla.social.core.network.model.paging.NetworkPageable

/**
 * Represents a hashtag used within the content of a status.
 */
@Serializable
data class NetworkHashTag(

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
    val history: List<NetworkHistory>? = null
) : NetworkPageable {

    override val id: String
        get() = name
}
