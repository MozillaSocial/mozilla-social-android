package social.firefly.core.network.mastodon.model

import kotlinx.serialization.Serializable

/**
 * Usage statistics about the Mastodon instance.
 */
@Serializable
data class NetworkInstanceStats(
    /**
     * Number of users registered on this instance.
     */
    val userCount: Long,
    /**
     * Number of statuses posted on this instance.
     */
    val statusCount: Long,
    /**
     * Number of domains federated with this instance.
     */
    val domainCount: Long,
)
