package org.mozilla.social.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface RecommendationApi {
    @GET("/desktop/v1/recommendations/")
    suspend fun getRecommendations(
        @Query("locale") locale: String,
        @Query("region") region: String,
        @Query("consumer_key") consumerKey: String,
        @Query("count") count: Int,
    ): RecommendationResponse

}

@Serializable
data class RecommendationResponse(
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