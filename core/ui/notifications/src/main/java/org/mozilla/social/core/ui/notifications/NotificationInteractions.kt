package org.mozilla.social.core.ui.notifications

interface NotificationInteractions {
    fun onAvatarClicked(accountId: String)
    fun onMentionClicked(statusId: String)
}