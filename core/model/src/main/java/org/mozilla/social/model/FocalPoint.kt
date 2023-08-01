package org.mozilla.social.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @see [Official docs](https://docs.joinmastodon.org/methods/statuses/media/#focal-points)
 */
data class FocalPoint(

    /**
     * x coordinate in [-1, +1]
     */
    val x: Double,

    /**
     * y coordinate in [-1, +1]
     */
    val y: Double
)
