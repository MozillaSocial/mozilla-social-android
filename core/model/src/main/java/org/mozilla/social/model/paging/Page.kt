package org.mozilla.social.model.paging

/**
 * A page of results from the API.
 *
 * To get the next or previous page, see [nextPage] and [previousPage].
 */
data class Page<T>(

    /**
     * The page contents.
     */
    val contents: T,

    /**
     * An object that can be passed to the calling API
     * to get the next page of contents.
     */
    val nextPage: PageInfo?,

    /**
     * An object that can be passed to the calling API
     * to get the previous page of contents.
     */
    val previousPage: PageInfo?
)
