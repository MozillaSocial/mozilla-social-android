package social.firefly.core.ui.notifications

interface NotificationInteractions {
    fun onAvatarClicked(accountId: String)
    fun onMentionClicked(statusId: String)
    fun onRepostClicked(statusId: String)
    fun onPollEndedClicked(statusId: String)
    fun onStatusUpdatedCardClicked(statusId: String)
    fun onNewStatusClicked(statusId: String)
    fun onFavoritedCardClicked(statusId: String)
    fun onFollowCardClicked(accountId: String)
    fun onFollowRequestCardClicked(accountId: String)
    fun onAcceptFollowRequestClicked(
        accountId: String,
        notificationId: Int,
    )
    fun onDenyFollowRequestClicked(
        accountId: String,
        notificationId: Int,
    )
}

object NotificationInteractionsNoOp : NotificationInteractions {
    override fun onAvatarClicked(accountId: String) = Unit
    override fun onMentionClicked(statusId: String) = Unit
    override fun onRepostClicked(statusId: String) = Unit
    override fun onPollEndedClicked(statusId: String) = Unit
    override fun onStatusUpdatedCardClicked(statusId: String) = Unit
    override fun onNewStatusClicked(statusId: String) = Unit
    override fun onFavoritedCardClicked(statusId: String) = Unit
    override fun onFollowCardClicked(accountId: String) = Unit
    override fun onAcceptFollowRequestClicked(accountId: String, notificationId: Int) = Unit
    override fun onDenyFollowRequestClicked(accountId: String, notificationId: Int) = Unit
    override fun onFollowRequestCardClicked(accountId: String) = Unit
}