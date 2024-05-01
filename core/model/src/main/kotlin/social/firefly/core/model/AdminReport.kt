package social.firefly.core.model

import kotlinx.datetime.Instant

data class AdminReport(
    val id: String,
    val actionTaken: Boolean,
    val actionTakenAt: Instant?,
    val category: String,
    val comment: String,
    val forwarded: Boolean,
    val createdAt: Instant,
    val statusIds: List<String>?,
    val ruleIds: List<String>?,
    val targetAccount: Account,
)
