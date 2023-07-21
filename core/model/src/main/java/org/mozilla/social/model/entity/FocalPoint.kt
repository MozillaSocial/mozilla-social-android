package org.mozilla.social.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @see [Official docs](https://docs.joinmastodon.org/methods/statuses/media/#focal-points)
 */
@Serializable
data class FocalPoint(

    /**
     * x coordinate in [-1, +1]
     */
    @SerialName("x")
    val x: Double,

    /**
     * y coordinate in [-1, +1]
     */
    @SerialName("y")
    val y: Double
)
