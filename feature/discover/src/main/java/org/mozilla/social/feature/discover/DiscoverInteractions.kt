package org.mozilla.social.feature.discover

interface DiscoverInteractions {
    fun onRetryClicked()
    fun onRepostClicked(recommendationId: String)
    fun onBookmarkClicked(recommendationId: String)
    fun onShareClicked(recommendationId: String)
    fun onRecommendationClicked(recommendationId: String)
    fun onScreenViewed()
    fun onRecommendationViewed(recommendationId: String)
    fun onSearchBarClicked()
}

object DiscoverInteractionsNoOp : DiscoverInteractions {
    override fun onRetryClicked() = Unit
    override fun onRepostClicked(recommendationId: String) = Unit
    override fun onBookmarkClicked(recommendationId: String) = Unit
    override fun onShareClicked(recommendationId: String) = Unit
    override fun onRecommendationClicked(recommendationId: String) = Unit
    override fun onScreenViewed() = Unit
    override fun onRecommendationViewed(recommendationId: String) = Unit
    override fun onSearchBarClicked() = Unit
}
