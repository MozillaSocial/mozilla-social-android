package org.mozilla.social.core.data.repository

import org.mozilla.social.core.data.BuildConfig
import org.mozilla.social.core.network.NetworkRecommendation
import org.mozilla.social.core.network.RecommendationApi
import org.mozilla.social.model.Recommendation

class RecommendationRepository(val recommendationApi: RecommendationApi) {

    suspend fun getRecommendations(): List<Recommendation> =
        recommendationApi.getRecommendations(
            locale = "en-US",
            region = "US",
            consumerKey = BuildConfig.newTabConsumerKey,
            count = 5
        ).recommendations.map { it.toExternalModel() }
}

private fun NetworkRecommendation.toExternalModel() = Recommendation(
    url = url,
    title = title,
    imageUrl = imageUrl,
    excerpt = excerpt,
    publisher = publisher,
    timeToRead = timeToRead?.let { "$it min read" }
)

