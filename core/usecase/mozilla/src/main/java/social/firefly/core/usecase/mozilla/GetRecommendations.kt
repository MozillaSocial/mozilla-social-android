package social.firefly.core.usecase.mozilla

import social.firefly.core.repository.mozilla.RecommendationRepository

class GetRecommendations(private val repository: RecommendationRepository) {
    suspend operator fun invoke() = repository.getRecommendations()
}
