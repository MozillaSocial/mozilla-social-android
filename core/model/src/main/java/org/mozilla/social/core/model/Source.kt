package org.mozilla.social.core.model

/**
 * Represents display or publishing preferences of the [Account].
 *
 * Returned as an additional entity when verifying and updated credentials.
 */
data class Source(

    /**
     * The account's bio.
     */
    val bio: String,

    /**
     * Metadata about the account as a list of name/values.
     */
    val fields: List<Field>,

    /**
     * The default post privacy to be used for new statuses.
     */
    val defaultPrivacy: StatusVisibility? = null,

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
