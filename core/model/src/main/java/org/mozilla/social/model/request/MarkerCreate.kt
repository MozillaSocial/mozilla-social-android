package org.mozilla.social.model.request

import org.mozilla.social.model.Marker

/**
 * Object used to create a new [Marker].
 */
data class MarkerCreate(

    /**
     * Marker for the home timeline.
     */
    val home: MarkerCreateProperties,

    /**
     * Marker for the notifications timeline.
     */
    val notifications: MarkerCreateProperties
)
