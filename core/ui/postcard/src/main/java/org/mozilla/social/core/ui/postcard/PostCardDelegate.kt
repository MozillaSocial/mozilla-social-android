package org.mozilla.social.core.ui.postcard

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.domain.account.BlockAccount
import org.mozilla.social.core.domain.account.MuteAccount
import org.mozilla.social.core.domain.status.BoostStatus
import org.mozilla.social.core.domain.status.FavoriteStatus
import org.mozilla.social.core.domain.status.UndoBoostStatus
import org.mozilla.social.core.domain.status.UndoFavoriteStatus
import org.mozilla.social.core.domain.status.VoteOnPoll
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.ui.common.R
import timber.log.Timber

class PostCardDelegate(
    private val coroutineScope: CoroutineScope,
    private val statusRepository: StatusRepository,
    private val navigateTo: NavigateTo,
    private val openLink: OpenLink,
    private val showSnackbar: ShowSnackbar,
    private val blockAccount: BlockAccount,
    private val muteAccount: MuteAccount,
    private val voteOnPoll: VoteOnPoll,
    private val boostStatus: BoostStatus,
    private val undoBoostStatus: UndoBoostStatus,
    private val favoriteStatus: FavoriteStatus,
    private val undoFavoriteStatus: UndoFavoriteStatus,
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
                statusRepository.deleteStatus(statusId)
            } catch (e: Exception) {
                Timber.e(e)
                showSnackbar(
                    text = StringFactory.resource(R.string.error_deleting_post),
                    isError = true,
                )
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