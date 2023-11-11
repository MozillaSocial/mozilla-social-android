package org.mozilla.social.core.model

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
