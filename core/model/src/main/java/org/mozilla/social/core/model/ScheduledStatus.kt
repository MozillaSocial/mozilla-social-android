package org.mozilla.social.core.model

import kotlinx.datetime.Instant

/**
 * Represents a status that will be published at a future scheduled date.
 */
data class ScheduledStatus(
    val statusId: String,
    /**
     * Time at which the status should be posted.
     */
    val scheduledAt: Instant,
    /**
     * Status details.
     */
    val params: ScheduledStatusParams,
    /**
     * Media attached to this status.
     */
    val mediaAttachments: List<Attachment>,
)
