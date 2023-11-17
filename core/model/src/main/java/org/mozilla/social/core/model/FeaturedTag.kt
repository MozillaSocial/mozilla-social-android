package org.mozilla.social.core.model

import kotlinx.datetime.LocalDate

/**
 * Represents a hashtag that is featured on a profile.
 */
data class FeaturedTag(
    val tagId: String,
    /**
     * The name of the hashtag being featured.
     */
    val name: String,
    /**
     * The number of authored statuses containing this hashtag.
     */
    val statusesCount: Long,
    /**
     * The time of the last authored status containing this hashtag.
     */
    val lastStatusAt: LocalDate,
)
