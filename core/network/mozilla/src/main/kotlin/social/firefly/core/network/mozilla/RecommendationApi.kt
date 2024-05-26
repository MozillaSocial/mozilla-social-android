package social.firefly.core.network.mozilla

import social.firefly.core.network.mozilla.model.NetworkRecommendations

interface RecommendationApi {

    suspend fun getRecommendations(
        locale: String,
        count: Int = 24,
        imageSizes: String = "200x",
    ): NetworkRecommendations
}
