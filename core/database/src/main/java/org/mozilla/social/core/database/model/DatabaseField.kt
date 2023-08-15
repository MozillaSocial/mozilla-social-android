package org.mozilla.social.core.database.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Profile metadata name and value.
 *
 * By default, max 4 fields and 255 characters per property/value.
 */
@Serializable
data class DatabaseField(
    val name: String,
    val value: String,

    /**
     * Timestamp of the time the server verified a URL value for a `rel="me”` link.
     */
    val verifiedAt: Instant? = null
)
