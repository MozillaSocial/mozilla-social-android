package org.mozilla.social.core.network.mastodon.model.paging

/**
 * Represents a pagination state, to get a specific page of results from an API.
 */
data class NetworkOffsetPageInfo(

    /**
     * The number of items to skip at the start of the queried list.
     */
    val offset: Int? = null
)
