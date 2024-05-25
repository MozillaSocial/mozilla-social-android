package social.firefly.core.network.mastodon.model.request

import social.firefly.core.network.mastodon.model.responseBody.NetworkMarker

/**
 * Object used to create a new [NetworkMarker].
 */
data class NetworkMarkerCreate(
    /**
     * Marker for the home timeline.
     */
    val home: NetworkMarkerCreateProperties,
    /**
     * Marker for the notifications timeline.
     */
    val notifications: NetworkMarkerCreateProperties,
)
