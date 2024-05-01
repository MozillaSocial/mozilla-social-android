package social.firefly.core.model

/**
 * Reports filed against users and/or statuses, to be taken action on by moderators.
 */
data class Report(
    val reportId: String,
    /**
     * Whether an action was taken to resolve this report.
     */
    val wasActionTaken: Boolean,
)
