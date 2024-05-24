package social.firefly.core.network.mastodon.model.responseBody

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
    val thumbnail: social.firefly.core.network.mastodon.model.responseBody.NetworkInstanceThumbnail,
    /**
     * A staff user that can be contacted, as an alternative to [email].
     */
    val contact: social.firefly.core.network.mastodon.model.responseBody.NetworkInstanceContact? = null,

    val rules: List<social.firefly.core.network.mastodon.model.responseBody.NetworkInstanceRule>
)

@Serializable
data class NetworkInstanceThumbnail(
    val url: String,
)

@Serializable
data class NetworkInstanceContact(
    val email: String,
    val account: social.firefly.core.network.mastodon.model.responseBody.NetworkAccount,
)
