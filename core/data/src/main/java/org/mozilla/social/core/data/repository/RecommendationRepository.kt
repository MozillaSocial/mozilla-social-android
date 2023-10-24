package org.mozilla.social.core.data.repository

import org.mozilla.social.core.data.repository.model.recommendations.toExternalModel
import org.mozilla.social.core.network.RecommendationApi
import org.mozilla.social.model.Recommendation
import java.util.Locale

class RecommendationRepository(
    private val recommendationApi: RecommendationApi
) {

    suspend fun getRecommendations(): List<Recommendation> =
        recommendationApi.getRecommendations(
            locale = "${Locale.getDefault().language}-${Locale.getDefault().country}",
        ).recommendations.map { it.toExternalModel() }
}
