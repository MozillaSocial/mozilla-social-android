package org.mozilla.social.core.ui.postcard

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mozilla.social.core.usecase.mastodon.status.BoostStatus
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.storage.mastodon.timeline.DeleteStatusFailedException
import org.mozilla.social.core.usecase.mastodon.account.BlockAccount
import org.mozilla.social.core.usecase.mastodon.account.MuteAccount
import org.mozilla.social.core.usecase.mastodon.status.DeleteStatusFromTimelines
import org.mozilla.social.core.usecase.mastodon.status.FavoriteStatus
import org.mozilla.social.core.usecase.mastodon.status.UndoBoostStatus
import org.mozilla.social.core.usecase.mastodon.status.UndoFavoriteStatus
import org.mozilla.social.core.usecase.mastodon.status.VoteOnPoll
import timber.log.Timber

class PostCardDelegate(
    private val coroutineScope: CoroutineScope,
    private val navigateTo: NavigateTo,
    private val openLink: OpenLink,
    private val blockAccount: BlockAccount,
    private val muteAccount: MuteAccount,
    private val voteOnPoll: VoteOnPoll,
    private val boostStatus: BoostStatus,
    private val undoBoostStatus: UndoBoostStatus,
    private val favoriteStatus: FavoriteStatus,
    private val undoFavoriteStatus: UndoFavoriteStatus,
    private val deleteStatusFromTimelines: DeleteStatusFromTimelines,
) : PostCardInteractions {

    override fun onVoteClicked(pollId: String, choices: List<Int>) {
        coroutineScope.launch {
            try {
                voteOnPoll(pollId, choices)
            } catch (e: VoteOnPoll.VoteOnPollFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onReplyClicked(statusId: String) {
        navigateTo(NavigationDestination.NewPost(replyStatusId = statusId))
    }

    override fun onBoostClicked(statusId: String, isBoosting: Boolean) {
        coroutineScope.launch {
            if (isBoosting) {
                try {
                    boostStatus(statusId)
                } catch (e: BoostStatus.BoostStatusFailedException) {
                    Timber.e(e)
                }
            } else {
                try {
                    undoBoostStatus(statusId)
                } catch (e: UndoBoostStatus.UndoBoostStatusFailedException) {
                    Timber.e(e)
                }
            }
        }
    }

    override fun onFavoriteClicked(statusId: String, isFavoriting: Boolean) {
        coroutineScope.launch {
            if (isFavoriting) {
                try {
                    favoriteStatus(statusId)
                } catch (e: FavoriteStatus.FavoriteStatusFailedException) {
                    Timber.e(e)
                }
            } else {
                try {
                    undoFavoriteStatus(statusId)
                } catch (e: UndoFavoriteStatus.UndoFavoriteStatusFailedException) {
                    Timber.e(e)
                }
            }
        }
    }

    override fun onPostCardClicked(statusId: String) {
        navigateTo(NavigationDestination.Thread(threadStatusId = statusId))
    }

    override fun onOverflowMuteClicked(accountId: String) {
        coroutineScope.launch {
            try {
                muteAccount(accountId)
            } catch (e: MuteAccount.MuteFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowBlockClicked(accountId: String) {
        coroutineScope.launch {
            try {
                blockAccount(accountId)
            } catch (e: BlockAccount.BlockFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowReportClicked(
        accountId: String,
        accountHandle: String,
        statusId: String,
    ) {
        navigateTo(NavigationDestination.Report(
            reportAccountId = accountId,
            reportAccountHandle = accountHandle,
            reportStatusId = statusId,
        ))
    }

    override fun onOverflowDeleteClicked(
        statusId: String,
    ) {
        coroutineScope.launch {
            try {
                deleteStatusFromTimelines(statusId)
            } catch (e: DeleteStatusFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onAccountImageClicked(accountId: String) {
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onLinkClicked(url: String) {
        openLink(url)
    }

    override fun onAccountClicked(accountId: String) {
        navigateTo(NavigationDestination.Account(accountId))

    }

    override fun onHashTagClicked(hashTag: String) {
        navigateTo(NavigationDestination.HashTag(hashTag))
    }
}