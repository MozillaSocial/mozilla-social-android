package social.firefly.core.network.mastodon.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRelationshipSeveranceEvent(
    @SerialName("id")
    val id: String,
    @SerialName("type")
    val type: String,
    @SerialName("purged")
    val purged: Boolean,
    @SerialName("target_name")
    val targetName: String,
    @SerialName("relationships_count")
    val relationshipsCount: Int?,
    @SerialName("created_at")
    val createdAt: Instant,
)