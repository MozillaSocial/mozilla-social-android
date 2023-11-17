package org.mozilla.social.core.network.mastodon.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Profile metadata name and value.
 *
 * By default, max 4 fields and 255 characters per property/value.
 */
@Serializable
data class NetworkField(
    @SerialName("name")
    val name: String,
    @SerialName("value")
    val value: String,
    /**
     * Timestamp of the time the server verified a URL value for a `rel="me”` link.
     */
    @SerialName("verified_at")
    val verifiedAt: Instant? = null,
)
