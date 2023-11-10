package org.mozilla.social.core.network.model

/**
 * Usage statistics about the Mastodon instance.
 */
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
