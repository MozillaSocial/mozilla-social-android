package org.mozilla.social.core.network.mastodon.model

/**
 * Reports filed against users and/or statuses, to be taken action on by moderators.
 */
data class NetworkReport(
    val reportId: String,
    /**
     * Whether an action was taken to resolve this report.
     */
    val wasActionTaken: Boolean,
)
