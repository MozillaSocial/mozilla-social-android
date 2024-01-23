package org.mozilla.social.core.ui.notifications

interface NotificationInteractions {
    fun onAvatarClicked(accountId: String)
    fun onMentionClicked(statusId: String)
    fun onRepostClicked(statusId: String)
    fun onPollEndedClicked(statusId: String)
    fun onStatusUpdatedCardClicked(statusId: String)
    fun onNewStatusClicked(statusId: String)
    fun onFavoritedCardClicked(statusId: String)
    fun onFollowRequestCardClicked(accountId: String)
    fun onAcceptFollowRequestClicked(accountId: String)
    fun onDenyFollowRequestClicked(accountId: String)
}

object NotificationInteractionsNoOp : NotificationInteractions {
    override fun onAvatarClicked(accountId: String) = Unit
    override fun onMentionClicked(statusId: String) = Unit
    override fun onRepostClicked(statusId: String) = Unit
    override fun onPollEndedClicked(statusId: String) = Unit
    override fun onStatusUpdatedCardClicked(statusId: String) = Unit
    override fun onNewStatusClicked(statusId: String) = Unit
    override fun onFavoritedCardClicked(statusId: String) = Unit
    override fun onAcceptFollowRequestClicked(accountId: String) = Unit
    override fun onDenyFollowRequestClicked(accountId: String) = Unit
    override fun onFollowRequestCardClicked(accountId: String) = Unit
}