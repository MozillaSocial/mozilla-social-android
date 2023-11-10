package org.mozilla.social.core.network.mastodon.model.request

/**
 * Object used to save the current position in a timeline.
 */
data class NetworkMarkerCreateProperties(

    /**
     * ID of the last status read in the timeline.
     */
    val lastReadId: String
)
