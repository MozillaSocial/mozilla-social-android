package org.mozilla.social.core.network.model

/**
 * Related instance URLs.
 */
data class NetworkInstanceUrls(

    /**
     * WebSockets API base URL.
     *
     * e.g. wss://mastodon.example
     */
    val streamingApiUrl: String
)
