package org.mozilla.social.feature.account

import org.mozilla.social.core.navigation.NavDestination
import org.mozilla.social.core.navigation.NavigateTo

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

class AccountNavigationCallbacksImpl(private val navigateTo: NavigateTo) : AccountNavigationCallbacks {
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