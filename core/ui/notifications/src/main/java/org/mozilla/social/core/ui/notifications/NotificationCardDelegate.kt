package org.mozilla.social.core.ui.notifications

import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo

class NotificationCardDelegate(
    private val navigateTo: NavigateTo,
) : NotificationInteractions {

    override fun onAvatarClicked(accountId: String) {
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onMentionClicked(statusId: String) {
        navigateTo(NavigationDestination.Thread(statusId))
    }

    override fun onRepostClicked(statusId: String) {
        navigateTo(NavigationDestination.Thread(statusId))
    }
}