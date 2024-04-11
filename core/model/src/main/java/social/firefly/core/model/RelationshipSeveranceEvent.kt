package social.firefly.core.model

import kotlinx.datetime.Instant

data class RelationshipSeveranceEvent(
    val id: String,
    val type: String,
    val purged: Boolean,
    val targetName: String,
    val relationshipsCount: Int?,
    val createdAt: Instant,
)