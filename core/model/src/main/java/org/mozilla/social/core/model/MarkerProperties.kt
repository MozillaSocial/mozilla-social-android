package org.mozilla.social.core.model

import kotlinx.datetime.Instant

/**
 * Marks the current reading position on a specific timeline.
 */
data class MarkerProperties(
    /**
     * ID of the last read item.
     */
    val lastReadId: String,
    /**
     * Date at which this marker was updated.
     */
    val updatedAt: Instant,
    /**
     * Used for locking to prevent write conflicts.
     */
    val version: Long,
)
