package org.mozilla.social.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents daily usage history of a hashtag.
 */

@Serializable
data class History(

    /**
     * Day the historical data was recorded on.
     */
    @SerialName("day")
    val day: String, // changing this to string for now.  Can't get deserializer to work right and I'm not actually using this property

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
