package org.mozilla.social.model

import org.mozilla.social.model.paging.Pageable
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
    val mediaAttachments: List<Attachment>
) : Pageable {

    override val id: String
        get() = statusId
}
