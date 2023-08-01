package org.mozilla.social.core.network.model

import kotlinx.datetime.Instant

/**
 * Represents a user-defined filter for determining
 * which statuses should not be shown to the user.
 */
data class NetworkFilter(
    val filterId: String,

    /**
     * The text to be filtered.
     */
    val phrase: String,

    /**
     * The contexts in which the filter should be applied.
     */
    val context: List<NetworkFilterContext>,

    /**
     * Should matching entities in home and notifications be dropped by the server?
     */
    val isIrreversible: Boolean,

    /**
     * Should the filter consider word boundaries?
     *
     * @see [Filter docs](https://docs.joinmastodon.org/entities/filter/)
     */
    val wholeWord: Boolean,

    /**
     * Time at which the filter should no longer be applied.
     */
    val expiresAt: Instant?
)
