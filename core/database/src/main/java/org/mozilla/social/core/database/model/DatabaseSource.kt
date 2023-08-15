package org.mozilla.social.core.database.model

import androidx.room.Embedded
import kotlinx.serialization.Serializable

/**
 * Represents display or publishing preferences of the [Account].
 *
 * Returned as an additional entity when verifying and updated credentials.
 */
@Serializable
data class DatabaseSource(

    /**
     * The account's bio.
     */
    val bio: String,

    /**
     * Metadata about the account as a list of name/values.
     */
//    @Embedded(prefix = "field_")
    val fields: List<DatabaseField>,

    /**
     * The default post privacy to be used for new statuses.
     */
    val defaultPrivacy: DatabaseStatusVisibility? = null,

    /**
     * Whether new statuses should be marked sensitive by default.
     */
    val defaultSensitivity: Boolean? = null,

    /**
     * The default posting language for new statuses.
     *
     * ISO 639-1 language two-letter code.
     */
    val defaultLanguage: String? = null,

    /**
     * The number of pending follow requests.
     */
    val followRequestsCount: Long? = null
)
