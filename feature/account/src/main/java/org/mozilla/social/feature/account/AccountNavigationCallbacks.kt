package org.mozilla.social.feature.account

import androidx.navigation.NavDestination

interface AccountNavigationCallbacks {
    fun onFollowingClicked(accountId: String)
    fun onFollowersClicked(accountId: String)
    fun onCloseClicked()
    fun onReportClicked(
        accountId: String,
        accountHandle: String,
    )

    fun navigateToSettings()
}

class AccountNavigationCallbacksImpl(private val navigateTo: NavigateTo<NavDestination>) : AccountNavigationCallbacks {
    override fun onFollowingClicked(accountId: String) {
    }

    override fun onFollowersClicked(accountId: String) {
        TODO("Not yet implemented")
    }

    override fun onCloseClicked() {
        TODO("Not yet implemented")
    }

    override fun onReportClicked(accountId: String, accountHandle: String) {
        TODO("Not yet implemented")
    }

    override fun navigateToSettings() {
        TODO("Not yet implemented")
    }
}