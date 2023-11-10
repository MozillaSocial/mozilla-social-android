package org.mozilla.social.core.network.mastodon.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the tree around a given status.
 *
 * Used for reconstructing threads of statuses.
 */
@Serializable
data class NetworkContext(

    /**
     * Parent statuses in the thread.
     */
    @SerialName("ancestors")
    val ancestors: List<NetworkStatus>,

    /**
     * Children statuses in the thread.
     */
    @SerialName("descendants")
    val descendants: List<NetworkStatus>
)
