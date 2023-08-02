package org.mozilla.social.core.network.model

/**
 * Represents the tree around a given status.
 *
 * Used for reconstructing threads of statuses.
 */
data class NetworkContext(

    /**
     * Parent statuses in the thread.
     */
    val ancestors: List<NetworkStatus>,

    /**
     * Children statuses in the thread.
     */
    val descendants: List<NetworkStatus>
)
