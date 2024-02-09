package org.mozilla.social.feature.followers

interface FollowersInteractions {
    fun onAccountClicked(accountId: String) = Unit
    fun onFollowClicked(accountId: String, isFollowing: Boolean) = Unit
    fun onScreenViewed() = Unit
    fun onTabClicked(tabType: FollowType) = Unit
}
