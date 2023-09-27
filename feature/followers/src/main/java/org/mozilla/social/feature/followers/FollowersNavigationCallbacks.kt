package org.mozilla.social.feature.followers

interface FollowersNavigationCallbacks {
    fun onCloseClicked() = Unit
    fun onAccountClicked(accountId: String) = Unit
}