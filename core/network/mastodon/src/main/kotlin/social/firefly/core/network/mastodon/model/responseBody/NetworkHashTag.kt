package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
     * If the user is following the hashtag
     */
    @SerialName("following")
    val following: Boolean,
    /**
     * Hashtag usage statistics for given days.
     */
    @SerialName("history")
    val history: List<social.firefly.core.network.mastodon.model.responseBody.NetworkHistory>? = null,
)