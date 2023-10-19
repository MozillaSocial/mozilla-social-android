package org.mozilla.social.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRecommendations(
    @SerialName("data") val recommendations: List<NetworkRecommendation>,
)

@Serializable
data class NetworkRecommendation(
    @SerialName("url") val url: String,
    @SerialName("title") val title: String,
    @SerialName("excerpt") val excerpt: String,
    @SerialName("publisher") val publisher: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("timeToRead") val timeToRead: Int? = null,
)