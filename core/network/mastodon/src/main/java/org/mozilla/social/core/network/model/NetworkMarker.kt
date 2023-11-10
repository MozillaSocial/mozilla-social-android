package org.mozilla.social.core.network.model

/**
 * Marks the user's current position in their timelines,
 * to synchronize and restore it across devices.
 */
data class NetworkMarker(

    /**
     * Home timeline marker.
     */
    val home: NetworkMarkerProperties,

    /**
     * Notifications timeline marker.
     */
    val notifications: NetworkMarkerProperties
)
