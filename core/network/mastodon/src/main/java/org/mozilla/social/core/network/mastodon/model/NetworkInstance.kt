package org.mozilla.social.core.network.mastodon.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Information about the software instance of Mastodon running on this domain.
 */
@Serializable
data class NetworkInstance(
    /**
     * The domain name of the instance.
     */
    @SerialName("domain")
    val domain: String,
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
     * URL of a banner image for the instance.
     */
    val thumbnail: NetworkInstanceThumbnail,
    /**
     * A staff user that can be contacted, as an alternative to [email].
     */
    val contact: NetworkInstanceContact? = null,
)

@Serializable
data class NetworkInstanceThumbnail(
    val url: String,
)

@Serializable
data class NetworkInstanceContact(
    val email: String,
    val account: NetworkAccount,
)
