package org.mozilla.social.core.repository.mozilla

import org.mozilla.social.core.model.Recommendation
import org.mozilla.social.core.network.mozilla.RecommendationApi
import org.mozilla.social.core.repository.mozilla.model.toExternalModel
import java.util.Locale

class RecommendationRepository(
    private val recommendationApi: RecommendationApi,
) {
    suspend fun getRecommendations(): List<Recommendation> =
        recommendationApi.getRecommendations(
            locale = Locale.getDefault().toLanguageTag(),
        ).recommendations.map { it.toExternalModel() }
}
