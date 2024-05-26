package social.firefly.core.network.mastodon.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkMediaAttributes(
    @SerialName("id")
    val id: String,
    @SerialName("description")
    val description: String? = null,
)
