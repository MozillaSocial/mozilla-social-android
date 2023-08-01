package org.mozilla.social.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Profile metadata name and value.
 *
 * By default, max 4 fields and 255 characters per property/value.
 */
data class Field(
    val name: String,
    val value: String,

    /**
     * Timestamp of the time the server verified a URL value for a `rel="me”` link.
     */
    val verifiedAt: Instant? = null
)
