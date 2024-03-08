package social.firefly.feature.discover

import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.ui.common.hashtag.HashtagInteractions

interface DiscoverInteractions: HashtagInteractions {
    fun onRetryClicked()
    fun onShareClicked(recommendationId: String)
    fun onRecommendationClicked(recommendationId: String)
    fun onScreenViewed()
    fun onSearchBarClicked()

    fun onTabClicked(tab: DiscoverTab)
}

object DiscoverInteractionsNoOp : DiscoverInteractions {
    override fun onRetryClicked() = Unit
    override fun onShareClicked(recommendationId: String) = Unit
    override fun onRecommendationClicked(recommendationId: String) = Unit
    override fun onScreenViewed() = Unit
    override fun onSearchBarClicked() = Unit
    override fun onTabClicked(tab: DiscoverTab) = Unit

    override fun onHashTagFollowClicked(name: String, followStatus: FollowStatus) = Unit
    override fun onHashtagClick(name: String) = Unit
}
