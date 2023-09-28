package org.mozilla.social.feature.followers

interface FollowersInteractions {
    fun onCloseClicked() = Unit
    fun onAccountClicked(accountId: String) = Unit
}