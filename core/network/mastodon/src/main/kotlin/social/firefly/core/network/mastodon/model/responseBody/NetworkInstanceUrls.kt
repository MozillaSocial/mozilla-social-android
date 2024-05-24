package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.serialization.Serializable

/**
 * Related instance URLs.
 */
@Serializable
data class NetworkInstanceUrls(
    /**
     * WebSockets API base URL.
     *
     * e.g. wss://mastodon.example
     */
    val streamingApiUrl: String,
)
