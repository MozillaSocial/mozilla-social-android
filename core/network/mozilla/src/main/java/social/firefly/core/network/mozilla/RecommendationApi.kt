package social.firefly.core.network.mozilla

import social.firefly.core.network.mozilla.model.NetworkRecommendations
import retrofit2.http.GET
import retrofit2.http.Query

interface RecommendationApi {
    @GET("/content-feed/moso/v1/discover/")
    suspend fun getRecommendations(
        @Query("locale") locale: String,
        @Query("count") count: Int = 24,
        @Query("image_sizes[]") imageSizes: String = "200x",
//        @Query("consumer_key") consumerKey: String,
    ): NetworkRecommendations
}
