package org.mozilla.social.model.entity

import kotlinx.datetime.LocalDate

/**
 * Represents daily usage history of a hashtag.
 */
data class History(

    /**
     * Day the historical data was recorded on.
     */
    val day: LocalDate,

    /**
     * The counted usage of the tag within that day.
     */
    val usageCount: Long,

    /**
     * the total of accounts using the tag within that day.
     */
    val accountCount: Long
)
