package org.mozilla.social.core.data.repository

import org.mozilla.social.core.data.repository.model.recommendations.toExternalModel
import org.mozilla.social.core.network.mozilla.RecommendationApi
import org.mozilla.social.model.Recommendation
import java.util.Locale

class RecommendationRepository(
    private val recommendationApi: org.mozilla.social.core.network.mozilla.RecommendationApi
) {

    suspend fun getRecommendations(): List<Recommendation> =
        recommendationApi.getRecommendations(
            locale = Locale.getDefault().toLanguageTag(),
        ).recommendations.map { it.toExternalModel() }
}
