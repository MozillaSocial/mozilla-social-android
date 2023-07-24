package org.mozilla.social.model.entity

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents daily usage history of a hashtag.
 */
@Serializable
public data class History(

    /**
     * Day the historical data was recorded on.
     */
    @SerialName("day")
    val day: LocalDate,

    /**
     * The counted usage of the tag within that day.
     */
    @SerialName("uses")
    val usageCount: Long,

    /**
     * the total of accounts using the tag within that day.
     */
    @SerialName("accounts")
    val accountCount: Long
)
