package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Marks the user's current position in their timelines,
 * to synchronize and restore it across devices.
 */
@Serializable
data class NetworkMarker(
    /**
     * Home timeline marker.
     */
    @SerialName("home")
    val home: NetworkMarkerProperties,
    /**
     * Notifications timeline marker.
     */
    @SerialName("notifications")
    val notifications: NetworkMarkerProperties,
)
