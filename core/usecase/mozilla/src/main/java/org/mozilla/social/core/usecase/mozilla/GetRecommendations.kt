package org.mozilla.social.core.usecase.mozilla

import org.mozilla.social.core.repository.mozilla.RecommendationRepository

class GetRecommendations(private val repository: RecommendationRepository) {

    suspend operator fun invoke() = repository.getRecommendations()

}