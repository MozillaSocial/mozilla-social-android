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
     * A shorter description of the instance defined by the admin.
     */
    val shortDescription: String,

    /**
     * An email that may be contacted for any inquiries.
     */
    val email: String,

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
     * Whether registrations are enabled.
     */
    val areRegistrationsEnabled: Boolean,

    /**
     * Whether registrations require moderator approval.
     */
    val isApprovalRequired: Boolean,

    /**
     * Whether invites are enabled.
     */
    val areInvitesEnabled: Boolean,

    /**
     * URLs of interest for clients apps.
     */
    val urls: InstanceUrls,

    /**
     * Statistics about how much information the instance contains.
     */
    val stats: InstanceStats,

    /**
     * URL of a banner image for the instance.
     */
    val thumbnail: String? = null,

    /**
     * A staff user that can be contacted, as an alternative to [email].
     */
    val contactAccount: Account? = null
)
