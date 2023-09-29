package org.mozilla.social.feature.account

interface AccountNavigationCallbacks {
    fun onFollowingClicked(accountId: String) = Unit
    fun onFollowersClicked(accountId: String) = Unit
    fun onCloseClicked() = Unit
    fun onReportClicked(accountId: String) = Unit
}