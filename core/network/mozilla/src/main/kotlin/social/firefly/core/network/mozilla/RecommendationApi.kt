package social.firefly.core.network.mozilla

import retrofit2.http.GET
import retrofit2.http.Query
import social.firefly.core.network.mozilla.model.NetworkRecommendations

interface RecommendationApi {
    @GET("/content-feed/Ff/v1/discover/")
    suspend fun getRecommendations(
        @Query("locale") locale: String,
        @Query("count") count: Int = 24,
        @Query("image_sizes[]") imageSizes: String = "200x",
//        @Query("consumer_key") consumerKey: String,
    ): NetworkRecommendations
}
