package org.mozilla.social.model

/**
 * Represents daily usage history of a hashtag.
 */

data class History(

    /**
     * Day the historical data was recorded on.
     */
    val day: String, // changing this to string for now.  Can't get deserializer to work right and I'm not actually using this property

    /**
     * The counted usage of the tag within that day.
     */
    val usageCount: Long,

    /**
     * the total of accounts using the tag within that day.
     */
    val accountCount: Long
)
