package org.mozilla.social.core.model

/**
 * Information about the software instance of Mastodon running on this domain.
 */
data class Instance(

    /**
     * The domain name of the instance.
     */
    val uri: String,

    /**
     * The title of the instance.
     */
    val title: String,

    /**
     * An admin-defined description of the instance.
     */
    val description: String,

    /**
     * The version of Mastodon installed on the instance.
     */
    val version: String,

    /**
     * Primary languages of the website and its staff.
     *
     * ISO 639-1 language two-letter code.
     */
    val languages: List<String>,

    /**
     * URL of a banner image for the instance.
     */
    val thumbnail: String? = null,

    /**
     * A staff user that can be contacted, as an alternative to [email].
     */
    val contactAccount: Account? = null,

    val contactEmail: String? = null,
)
