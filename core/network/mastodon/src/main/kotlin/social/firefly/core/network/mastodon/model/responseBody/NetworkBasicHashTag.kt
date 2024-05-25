package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a hashtag used within the content of a status.
 */
@Serializable
data class NetworkBasicHashTag(
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
)
