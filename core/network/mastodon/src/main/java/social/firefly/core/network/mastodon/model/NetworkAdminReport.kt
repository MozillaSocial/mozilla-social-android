package social.firefly.core.network.mastodon.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAdminReport(
    @SerialName("id")
    val id: String,
    @SerialName("action_taken")
    val actionTaken: Boolean,
    @SerialName("action_taken_at")
    val actionTakenAt: Instant?,
    @SerialName("category")
    val category: String,
    @SerialName("comment")
    val comment: String,
    @SerialName("forwarded")
    val forwarded: Boolean,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("status_ids")
    val statusIds: List<String>?,
    @SerialName("rule_ids")
    val ruleIds: List<String>?,
    @SerialName("target_account")
    val targetAccount: NetworkAccount,
)
