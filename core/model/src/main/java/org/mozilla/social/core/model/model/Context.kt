package org.mozilla.social.model

/**
 * Represents the tree around a given status.
 *
 * Used for reconstructing threads of statuses.
 */
data class Context(

    /**
     * Parent statuses in the thread.
     */
    val ancestors: List<Status>,

    /**
     * Children statuses in the thread.
     */
    val descendants: List<Status>
)
