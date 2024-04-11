package social.firefly.feature.followers

import social.firefly.core.ui.common.following.FollowStatus

interface FollowersInteractions {
    fun onAccountClicked(accountId: String) = Unit
    fun onFollowClicked(accountId: String, followStatus: FollowStatus) = Unit
    fun onScreenViewed() = Unit
    fun onTabClicked(tabType: FollowType) = Unit
}
