package social.firefly.core.network.mozilla.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRecommendations(
    @SerialName("data") val recommendations: List<NetworkRecommendation>,
)

@Serializable
data class NetworkRecommendation(
    @SerialName("id") val id: String,
    @SerialName("url") val url: String,
    @SerialName("title") val title: String,
    @SerialName("excerpt") val excerpt: String,
    @SerialName("publisher") val publisher: String,
    @SerialName("image") val image: NetworkRecommendationImageSizes,
    @SerialName("authors") val authors: List<NetworkRecommendationAuthor>,
)

@Serializable
data class NetworkRecommendationImageSizes(
    @SerialName("sizes") val sizes: List<NetworkRecommendationImage>,
)

@Serializable
data class NetworkRecommendationImage(
    @SerialName("url") val url: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
)

@Serializable
data class NetworkRecommendationAuthor(
    @SerialName("name") val name: String,
)
