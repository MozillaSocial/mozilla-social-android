package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Visibility of a [NetworkStatus] to other users.
 */
@Serializable
enum class NetworkStatusVisibility(val value: String) {
    /**
     * Visible to everyone, shown in public timelines.
     */
    @SerialName("public")
    Public("public"),

    /**
     * Visible to public, but not included in public timelines.
     */
    @SerialName("unlisted")
    Unlisted("unlisted"),

    /**
     * Visible to followers only, and to any mentioned users.
     */
    @SerialName("private")
    Private("private"),

    /**
     * Visible only to mentioned users.
     */
    @SerialName("direct")
    Direct("direct"),
}
