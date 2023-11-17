package org.mozilla.social.core.network.mastodon.model.request

import org.mozilla.social.core.network.mastodon.model.NetworkMarker

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
