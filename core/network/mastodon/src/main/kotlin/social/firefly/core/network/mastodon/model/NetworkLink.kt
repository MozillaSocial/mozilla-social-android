package social.firefly.core.network.mastodon.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkLink(
    val url: String,
    val title: String,
    val description: String,
)