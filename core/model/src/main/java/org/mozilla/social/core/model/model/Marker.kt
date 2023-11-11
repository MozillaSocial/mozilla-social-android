package org.mozilla.social.model

/**
 * Marks the user's current position in their timelines,
 * to synchronize and restore it across devices.
 */
data class Marker(

    /**
     * Home timeline marker.
     */
    val home: MarkerProperties,

    /**
     * Notifications timeline marker.
     */
    val notifications: MarkerProperties
)
