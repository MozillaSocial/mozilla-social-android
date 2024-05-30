package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkError(
    @SerialName("error")
    val error: String,
)
