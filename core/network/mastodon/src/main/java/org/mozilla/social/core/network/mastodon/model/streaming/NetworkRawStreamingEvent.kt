package org.mozilla.social.core.network.mastodon.model.streaming

internal data class NetworkRawStreamingEvent(
    val event: String,
    val payload: String? = null
)
