package social.firefly.core.model

/**
 * Usage statistics about the Mastodon instance.
 */
data class InstanceStats(
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
