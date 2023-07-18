package org.mozilla.social.model.entity.streaming

internal data class RawStreamingEvent(
    val event: String,
    val payload: String? = null
)
