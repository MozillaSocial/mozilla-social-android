package org.mozilla.social.core.ui.postcard

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mozilla.social.core.analytics.FeedLocation
import org.mozilla.social.core.analytics.PostCardAnalytics
import org.mozilla.social.core.model.Attachment
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.usecase.mastodon.account.BlockAccount
import org.mozilla.social.core.usecase.mastodon.account.MuteAccount
import org.mozilla.social.core.usecase.mastodon.status.BoostStatus
import org.mozilla.social.core.usecase.mastodon.status.DeleteStatus
import org.mozilla.social.core.usecase.mastodon.status.FavoriteStatus
import org.mozilla.social.core.usecase.mastodon.status.UndoBoostStatus
import org.mozilla.social.core.usecase.mastodon.status.UndoFavoriteStatus
import org.mozilla.social.core.usecase.mastodon.status.VoteOnPoll
import timber.log.Timber

class PostCardDelegate(
    private val coroutineScope: CoroutineScope,
    feedLocation: FeedLocation,
    private val navigateTo: NavigateTo,
    private val openLink: OpenLink,
    private val blockAccount: BlockAccount,
    private val muteAccount: MuteAccount,
    private val voteOnPoll: VoteOnPoll,
    private val boostStatus: BoostStatus,
    private val undoBoostStatus: UndoBoostStatus,
    private val favoriteStatus: FavoriteStatus,
    private val undoFavoriteStatus: UndoFavoriteStatus,
    private val deleteStatus: DeleteStatus,
    private val analytics: PostCardAnalytics,
) : PostCardInteractions {

    private val baseAnalyticsIdentifier: String = feedLocation.baseAnalyticsIdentifier
    override fun onVoteClicked(
        pollId: String,
        choices: List<Int>,
    ) {
        coroutineScope.launch {
            try {
                analytics
                voteOnPoll(pollId, choices)
            } catch (e: VoteOnPoll.VoteOnPollFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onReplyClicked(statusId: String) {
        analytics.replyClicked(baseAnalyticsIdentifier)
        navigateTo(NavigationDestination.NewPost(replyStatusId = statusId))
    }

    override fun onBoostClicked(
        statusId: String,
        isBoosting: Boolean,
    ) {
        coroutineScope.launch {
            if (isBoosting) {
                try {
                    analytics.boostClicked(baseAnalyticsIdentifier)
                    boostStatus(statusId)
                } catch (e: BoostStatus.BoostStatusFailedException) {
                    Timber.e(e)
                }
            } else {
                try {
                    analytics.unboostClicked(baseAnalyticsIdentifier)
                    undoBoostStatus(statusId)
                } catch (e: UndoBoostStatus.UndoBoostStatusFailedException) {
                    Timber.e(e)
                }
            }
        }
    }

    override fun onFavoriteClicked(
        statusId: String,
        isFavoriting: Boolean,
    ) {
        coroutineScope.launch {
            if (isFavoriting) {
                try {
                    analytics.favoriteClicked(baseAnalyticsIdentifier)
                    favoriteStatus(statusId)
                } catch (e: FavoriteStatus.FavoriteStatusFailedException) {
                    Timber.e(e)
                }
            } else {
                try {
                    analytics.unfavoriteClicked(baseAnalyticsIdentifier)
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

    override fun onOverflowMuteClicked(
        accountId: String,
        statusId: String,
    ) {
        analytics.muteClicked(baseAnalyticsIdentifier)

        coroutineScope.launch {
            try {
                muteAccount(accountId)
            } catch (e: MuteAccount.MuteFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowBlockClicked(
        accountId: String,
        statusId: String,
    ) {
        analytics.blockClicked(baseAnalyticsIdentifier)
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

        analytics.reportClicked(baseAnalyticsIdentifier)

        navigateTo(
            NavigationDestination.Report(
                reportAccountId = accountId,
                reportAccountHandle = accountHandle,
                reportStatusId = statusId,
            ),
        )
    }

    override fun onOverflowDeleteClicked(statusId: String) {
        coroutineScope.launch {
            try {
                deleteStatus(statusId)
            } catch (e: DeleteStatus.DeleteStatusFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowEditClicked(statusId: String) {
        navigateTo(NavigationDestination.NewPost(editStatusId = statusId))
    }

    override fun onAccountImageClicked(accountId: String) {
        analytics.accountImageClicked(baseAnalyticsIdentifier)
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onLinkClicked(url: String) {
        analytics.onLinkClicked()
        openLink(url)
    }

    override fun onAccountClicked(accountId: String) {
        analytics.accountClicked()
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onHashTagClicked(hashTag: String) {
        analytics.hashtagClicked()

        navigateTo(NavigationDestination.HashTag(hashTag))
    }

    override fun onMediaClicked(attachments: List<Attachment>, index: Int) {
        navigateTo(
            NavigationDestination.Media(
                attachments = attachments,
                startIndex = index,
            )
        )
    }
}
