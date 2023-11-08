package org.mozilla.social.core.ui.postcard

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.ui.common.R
import timber.log.Timber

class PostCardDelegate(
    private val coroutineScope: CoroutineScope,
    private val statusRepository: StatusRepository,
    private val accountRepository: AccountRepository,
    private val navigateTo: NavigateTo,
    private val openLink: OpenLink,
    private val showSnackbar: ShowSnackbar,
) : PostCardInteractions {

    override fun onVoteClicked(pollId: String, choices: List<Int>) {
        coroutineScope.launch {
            try {
                statusRepository.voteOnPoll(pollId, choices)
            } catch (e: Exception) {
                Timber.e(e)
                showSnackbar(
                    text = StringFactory.resource(R.string.error_voting),
                    isError = true,
                )
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
                    statusRepository.boostStatus(statusId)
                } catch (e: Exception) {
                    Timber.e(e)
                    showSnackbar(
                        text = StringFactory.resource(R.string.error_boosting),
                        isError = true,
                    )
                }
            } else {
                try {
                    statusRepository.undoStatusBoost(statusId)
                } catch (e: Exception) {
                    Timber.e(e)
                    showSnackbar(
                        text = StringFactory.resource(R.string.error_undoing_boost),
                        isError = true,
                    )
                }
            }
        }
    }

    override fun onFavoriteClicked(statusId: String, isFavoriting: Boolean) {
        coroutineScope.launch {
            if (isFavoriting) {
                try {
                    statusRepository.favoriteStatus(statusId)
                } catch (e: Exception) {
                    Timber.e(e)
                    showSnackbar(
                        text = StringFactory.resource(R.string.error_adding_favorite),
                        isError = true,
                    )
                }
            } else {
                try {
                    statusRepository.undoFavoriteStatus(statusId)
                } catch (e: Exception) {
                    Timber.e(e)
                    showSnackbar(
                        text = StringFactory.resource(R.string.error_removing_favorite),
                        isError = true,
                    )
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
                accountRepository.muteAccount(accountId)
            } catch (e: Exception) {
                Timber.e(e)
                showSnackbar(
                    text = StringFactory.resource(R.string.error_muting_account),
                    isError = true,
                )
            }
        }
    }

    override fun onOverflowBlockClicked(accountId: String) {
        coroutineScope.launch {
            try {
                accountRepository.blockAccount(accountId)
            } catch (e: Exception) {
                Timber.e(e)
                showSnackbar(
                    text = StringFactory.resource(R.string.error_blocking_account),
                    isError = true,
                )
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