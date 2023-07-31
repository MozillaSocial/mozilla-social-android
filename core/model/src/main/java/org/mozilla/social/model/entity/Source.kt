package org.mozilla.social.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents display or publishing preferences of the [Account].
 *
 * Returned as an additional entity when verifying and updated credentials.
 */
@Serializable
data class Source(

    /**
     * The account's bio.
     */
    @SerialName("note")
    val bio: String,

    /**
     * Metadata about the account as a list of name/values.
     */
    @SerialName("fields")
    val fields: List<Field>,

    /**
     * The default post privacy to be used for new statuses.
     */
    @SerialName("privacy")
    val defaultPrivacy: StatusVisibility? = null,

    /**
     * Whether new statuses should be marked sensitive by default.
     */
    @SerialName("sensitive")
    val defaultSensitivity: Boolean? = null,

    /**
     * The default posting language for new statuses.
     *
     * ISO 639-1 language two-letter code.
     */
    @SerialName("language")
    val defaultLanguage: String? = null,

    /**
     * The number of pending follow requests.
     */
    @SerialName("follow_requests_count")
    val followRequestsCount: Long? = null
)
