package org.mozilla.social.core.network.mastodon.model.paging

/**
 * A page of results from the API.
 *
 * To get the next or previous page, see [nextPage] and [previousPage].
 */
data class NetworkPage<T>(
    /**
     * The page contents.
     */
    val contents: T,
    /**
     * An object that can be passed to the calling API
     * to get the next page of contents.
     */
    val nextPage: NetworkPageInfo?,
    /**
     * An object that can be passed to the calling API
     * to get the previous page of contents.
     */
    val previousPage: NetworkPageInfo?,
)
