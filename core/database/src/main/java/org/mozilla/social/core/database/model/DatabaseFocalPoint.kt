package org.mozilla.social.core.database.model

/**
 * @see [Official docs](https://docs.joinmastodon.org/methods/statuses/media/#focal-points)
 */
data class DatabaseFocalPoint(

    /**
     * x coordinate in [-1, +1]
     */
    val x: Double,

    /**
     * y coordinate in [-1, +1]
     */
    val y: Double
)
