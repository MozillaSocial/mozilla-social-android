package org.mozilla.social.feature.account

interface AccountNavigationCallbacks {
    fun onFollowingClicked() = Unit
    fun onFollowersClicked() = Unit
    fun onCloseClicked() = Unit
    fun onReportClicked(accountId: String) = Unit
}