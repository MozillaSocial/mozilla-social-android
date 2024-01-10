package org.mozilla.social.feature.settings.contentpreferences.mutedusers

interface MutedUsersInteractions {
    fun onScreenViewed()
    fun onButtonClicked(accountId: String, mutedButtonState: MutedButtonState)
    fun onAccountClicked(accountId: String)
}