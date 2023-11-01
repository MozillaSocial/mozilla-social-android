package org.mozilla.social.feature.followers

interface FollowersInteractions {
    fun onAccountClicked(accountId: String) = Unit
}