package social.firefly.core.network.mastodon.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Marks the current reading position on a specific timeline.
 */
@Serializable
data class NetworkMarkerProperties(
    /**
     * ID of the last read item.
     */
    @SerialName("last_read_id")
    val lastReadId: String,
    /**
     * Date at which this marker was updated.
     */
    @SerialName("updated_at")
    val updatedAt: Instant,
    /**
     * Used for locking to prevent write conflicts.
     */
    @SerialName("version")
    val version: Long,
)
