package org.mozilla.social.core.ui.notifications

interface NotificationInteractions {
    fun onAvatarClicked(accountId: String)
    fun onMentionClicked(statusId: String)
    fun onRepostClicked(statusId: String)
    fun onPollEndedClicked(statusId: String)
}

object NotificationInteractionsNoOp : NotificationInteractions {
    override fun onAvatarClicked(accountId: String) = Unit
    override fun onMentionClicked(statusId: String) = Unit
    override fun onRepostClicked(statusId: String) = Unit
    override fun onPollEndedClicked(statusId: String) = Unit
}