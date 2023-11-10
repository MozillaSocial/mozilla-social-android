package org.mozilla.social.core.network.mastodon.model

import kotlinx.serialization.SerialName

/**
 * Information about the software instance of Mastodon running on this domain.
 */
data class NetworkInstance(

    /**
     * The domain name of the instance.
     */
    @SerialName("uri")
    val uri: String,

    /**
     * The title of the instance.
     */
    @SerialName("title")
    val title: String,

    /**
     * An admin-defined description of the instance.
     */
    @SerialName("description")
    val description: String,

    /**
     * A shorter description of the instance defined by the admin.
     */
    @SerialName("shortDescription")
    val shortDescription: String,

    /**
     * An email that may be contacted for any inquiries.
     */
    @SerialName("email")
    val email: String,

    /**
     * The version of Mastodon installed on the instance.
     */
    @SerialName("version")
    val version: String,

    /**
     * Primary languages of the website and its staff.
     *
     * ISO 639-1 language two-letter code.
     */
    @SerialName("languages")
    val languages: List<String>,

    /**
     * Whether registrations are enabled.
     */
    @SerialName("areRegistrationsEnabled")
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
    val urls: NetworkInstanceUrls,

    /**
     * Statistics about how much information the instance contains.
     */
    val stats: NetworkInstanceStats,

    /**
     * URL of a banner image for the instance.
     */
    val thumbnail: String? = null,

    /**
     * A staff user that can be contacted, as an alternative to [email].
     */
    val contactAccount: NetworkAccount? = null
)
