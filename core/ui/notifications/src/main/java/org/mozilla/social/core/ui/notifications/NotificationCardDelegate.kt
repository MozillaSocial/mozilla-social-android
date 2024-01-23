package org.mozilla.social.core.ui.notifications

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.usecase.mastodon.followRequest.AcceptFollowRequest
import org.mozilla.social.core.usecase.mastodon.followRequest.DenyFollowRequest
import timber.log.Timber

class NotificationCardDelegate(
    private val coroutineScope: CoroutineScope,
    private val navigateTo: NavigateTo,
    private val acceptFollowRequest: AcceptFollowRequest,
    private val denyFollowRequest: DenyFollowRequest,
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

    override fun onPollEndedClicked(statusId: String) {
        navigateTo(NavigationDestination.Thread(statusId))
    }

    override fun onStatusUpdatedCardClicked(statusId: String) {
        navigateTo(NavigationDestination.Thread(statusId))
    }

    override fun onNewStatusClicked(statusId: String) {
        navigateTo(NavigationDestination.Thread(statusId))
    }

    override fun onFavoritedCardClicked(statusId: String) {
        navigateTo(NavigationDestination.Thread(statusId))
    }

    override fun onFollowCardClicked(accountId: String) {
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onFollowRequestCardClicked(accountId: String) {
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onAcceptFollowRequestClicked(
        accountId: String,
        notificationId: Int,
    ) {
        coroutineScope.launch {
            try {
                acceptFollowRequest(accountId, notificationId)
            } catch (e: AcceptFollowRequest.AcceptRequestFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onDenyFollowRequestClicked(
        accountId: String,
        notificationId: Int,
    ) {
        coroutineScope.launch {
            try {
                denyFollowRequest(accountId, notificationId)
            } catch (e: DenyFollowRequest.DenyRequestFailedException) {
                Timber.e(e)
            }
        }
    }
}