package org.mozilla.social.core.network.model.request

import org.mozilla.social.core.network.model.NetworkMarker

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
    val notifications: NetworkMarkerCreateProperties
)
