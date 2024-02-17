package social.firefly.core.repository.mozilla

import social.firefly.core.model.Recommendation
import social.firefly.core.network.mozilla.RecommendationApi
import social.firefly.core.repository.mozilla.model.toExternalModel
import java.util.Locale

class RecommendationRepository(
    private val recommendationApi: RecommendationApi,
) {
    suspend fun getRecommendations(): List<Recommendation> =
        recommendationApi.getRecommendations(
            locale = Locale.getDefault().toLanguageTag(),
        ).recommendations.map { it.toExternalModel() }
}
