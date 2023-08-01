package org.mozilla.social.model.request

/**
 * Object used to save the current position in a timeline.
 */
data class MarkerCreateProperties(

    /**
     * ID of the last status read in the timeline.
     */
    val lastReadId: String
)
