package social.firefly.core.model

/**
 * Related instance URLs.
 */
data class InstanceUrls(
    /**
     * WebSockets API base URL.
     *
     * e.g. wss://mastodon.example
     */
    val streamingApiUrl: String,
)
