package social.firefly.core.network.mastodon.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkReportCreate(
    @SerialName("account_id")
    val accountId: String,
    @SerialName("status_ids")
    val statusIds: List<String>?,
    @SerialName("comment")
    val comment: String?,
    @SerialName("forward")
    val forward: Boolean?,
    @SerialName("category")
    val category: String?,
    @SerialName("rule_ids")
    val ruleViolations: List<Int>?,
)
