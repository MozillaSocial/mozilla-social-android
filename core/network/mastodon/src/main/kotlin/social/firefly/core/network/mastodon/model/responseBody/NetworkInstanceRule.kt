package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkInstanceRule(
    @SerialName("id")
    val id: Int,
    @SerialName("text")
    val text: String,
)
