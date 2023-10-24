package org.mozilla.social.core.network

import org.mozilla.social.core.network.model.NetworkRecommendations
import retrofit2.http.GET
import retrofit2.http.Query

interface RecommendationApi {
    @GET("/desktop/v1/recommendations/")
    suspend fun getRecommendations(
        @Query("locale") locale: String,
        @Query("count") count: Int = 24,
        @Query("consumer_key") consumerKey: String,
    ): NetworkRecommendations
}
