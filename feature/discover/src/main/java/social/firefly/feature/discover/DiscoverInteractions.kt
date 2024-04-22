package social.firefly.feature.discover

import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.ui.common.hashtag.HashtagInteractions

interface DiscoverInteractions: HashtagInteractions {
    fun onScreenViewed()
    fun onSearchBarClicked()
    fun onTabClicked(tab: DiscoverTab)
}

object DiscoverInteractionsNoOp : DiscoverInteractions {
    override fun onScreenViewed() = Unit
    override fun onSearchBarClicked() = Unit
    override fun onTabClicked(tab: DiscoverTab) = Unit

    override fun onHashTagFollowClicked(name: String, followStatus: FollowStatus) = Unit
    override fun onHashtagClick(name: String) = Unit
}
