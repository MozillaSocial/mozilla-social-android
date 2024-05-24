package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.serialization.Serializable

@Serializable
data class NetworkLink(
    val url: String,
    val title: String,
    val description: String,
)