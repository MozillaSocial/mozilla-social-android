package org.mozilla.social.core.database.model

import kotlinx.serialization.Serializable

/**
 * Represents daily usage history of a hashtag.
 */
@Serializable
data class DatabaseHistory(
    // changing this to string for now.  Can't get deserializer to work right and I'm not
    // actually using this property
    /**
     * Day the historical data was recorded on.
     */
    val day: String,

    /**
     * The counted usage of the tag within that day.
     */
    val usageCount: Long,
    /**
     * the total of accounts using the tag within that day.
     */
    val accountCount: Long,
)
