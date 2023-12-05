package org.mozilla.social.feature.discover

interface DiscoverInteractions {
    fun onRetryClicked() = Unit

    fun onRepostClicked(recommendationId: String) = Unit

    fun onBookmarkClicked(recommendationId: String) = Unit

    fun onShareClicked(recommendationId: String) = Unit

    fun onRecommendationClicked(recommendationId: String) = Unit

    fun onScreenViewed() = Unit

    fun onRecommendationViewed(recommendationId: String) = Unit
}
