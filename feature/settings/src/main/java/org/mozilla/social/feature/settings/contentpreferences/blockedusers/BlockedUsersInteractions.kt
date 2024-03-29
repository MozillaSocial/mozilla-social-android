package org.mozilla.social.feature.settings.contentpreferences.blockedusers

interface BlockedUsersInteractions {
    fun onScreenViewed()
    fun onButtonClicked(accountId: String, buttonState: BlockedButtonState)
    fun onAccountClicked(accountId: String)
}