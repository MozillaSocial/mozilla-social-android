package org.mozilla.social.core.network.mastodon.model

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
    val streamingApiUrl: String
)
