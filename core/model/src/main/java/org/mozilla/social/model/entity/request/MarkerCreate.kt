package org.mozilla.social.model.entity.request

import org.mozilla.social.model.entity.Marker

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
