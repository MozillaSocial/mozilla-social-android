package social.firefly.core.network.mastodon.model

import kotlinx.datetime.Instant

/**
 * Admin-level information about a filed report.
 */
data class NetworkAdminReport(
    val reportId: String,
    /**
     * Whether an action was taken to resolve this report.
     */
    val wasActionTaken: Boolean,
    /**
     * An optional reason for reporting.
     */
    val comment: String,
    /**
     * The moment the report was filed.
     */
    val createdAt: Instant,
    /**
     * The time of last action on this report.
     */
    val updatedAt: Instant,
    /**
     * The account which filed the report.
     */
    val account: NetworkAdminAccount,
    /**
     * The account being reported.
     */
    val targetAccount: NetworkAdminAccount,
    /**
     * Statuses attached to the report, for context.
     */
    val statuses: List<NetworkStatus>,
    /**
     * The account of the moderator assigned to this report.
     */
    val assignedAccount: NetworkAdminAccount? = null,
    /**
     * The account of the moderator who handled the report.
     */
    val actionTakenByAccount: NetworkAdminAccount? = null,
)
