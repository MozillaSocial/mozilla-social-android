package org.mozilla.social.core.network.model.streaming

internal data class NetworkRawStreamingEvent(
    val event: String,
    val payload: String? = null
)
