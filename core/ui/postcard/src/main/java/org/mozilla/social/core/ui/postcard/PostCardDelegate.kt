package org.mozilla.social.core.ui.postcard

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.analytics.EngagementType
import org.mozilla.social.core.usecase.mastodon.status.BoostStatus
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.usecase.mastodon.account.BlockAccount
import org.mozilla.social.core.usecase.mastodon.account.MuteAccount
import org.mozilla.social.core.usecase.mastodon.status.DeleteStatus
import org.mozilla.social.core.usecase.mastodon.status.FavoriteStatus
import org.mozilla.social.core.usecase.mastodon.status.UndoBoostStatus
import org.mozilla.social.core.usecase.mastodon.status.UndoFavoriteStatus
import org.mozilla.social.core.usecase.mastodon.status.VoteOnPoll
import timber.log.Timber

class PostCardDelegate(
    private val coroutineScope: CoroutineScope,
    private val baseAnalyticsIdentifier: String,
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
    private val analytics: Analytics,
) : PostCardInteractions {
    override fun onVoteClicked(
        pollId: String,
        choices: List<Int>,
    ) {
        coroutineScope.launch {
            try {
                analytics.uiEngagement(
                    engagementType = EngagementType.GENERAL,
                    uiIdentifier = "$baseAnalyticsIdentifier.${AnalyticsIdentifiers.FEED_POST_VOTE}",
                    uiAdditionalDetail = pollId
                )
                voteOnPoll(pollId, choices)
            } catch (e: VoteOnPoll.VoteOnPollFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onReplyClicked(statusId: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = "$baseAnalyticsIdentifier.${AnalyticsIdentifiers.FEED_POST_REPLY}",
            mastodonStatusId = statusId
        )
        navigateTo(NavigationDestination.NewPost(replyStatusId = statusId))
    }

    override fun onBoostClicked(
        statusId: String,
        isBoosting: Boolean,
    ) {
        coroutineScope.launch {
            if (isBoosting) {
                try {
                    analytics.uiEngagement(
                        engagementType = EngagementType.GENERAL,
                        uiIdentifier = "$baseAnalyticsIdentifier.${AnalyticsIdentifiers.FEED_POST_BOOST}",
                        mastodonStatusId = statusId
                    )
                    boostStatus(statusId)
                } catch (e: BoostStatus.BoostStatusFailedException) {
                    Timber.e(e)
                }
            } else {
                try {
                    analytics.uiEngagement(
                        engagementType = EngagementType.GENERAL,
                        uiIdentifier = "$baseAnalyticsIdentifier.${AnalyticsIdentifiers.FEED_POST_UNBOOST}",
                        mastodonStatusId = statusId
                    )
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
                    analytics.uiEngagement(
                        engagementType = EngagementType.FAVORITE,
                        uiIdentifier = "$baseAnalyticsIdentifier.${AnalyticsIdentifiers.FEED_POST_FAVORITE}",
                        mastodonStatusId = statusId
                    )
                    favoriteStatus(statusId)
                } catch (e: FavoriteStatus.FavoriteStatusFailedException) {
                    Timber.e(e)
                }
            } else {
                try {
                    analytics.uiEngagement(
                        engagementType = EngagementType.FAVORITE,
                        uiIdentifier = "$baseAnalyticsIdentifier.${AnalyticsIdentifiers.FEED_POST_UNFAVORITE}",
                        mastodonStatusId = statusId
                    )
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
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = "$baseAnalyticsIdentifier.${AnalyticsIdentifiers.FEED_POST_MUTE}",
            mastodonStatusId = statusId
        )
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
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = "$baseAnalyticsIdentifier.${AnalyticsIdentifiers.FEED_POST_BLOCK}",
            mastodonStatusId = statusId
        )
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

        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = "$baseAnalyticsIdentifier.${AnalyticsIdentifiers.FEED_POST_REPORT}",
            mastodonStatusId = statusId
        )
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

    override fun onAccountImageClicked(accountId: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = AnalyticsIdentifiers.FEED_POST_ACCOUNT_IMAGE_TAPPED,
            mastodonAccountId = accountId
        )
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onLinkClicked(url: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = AnalyticsIdentifiers.FEED_POST_LINK_TAPPED,
            uiAdditionalDetail = url
        )
        openLink(url)
    }

    override fun onAccountClicked(accountId: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = AnalyticsIdentifiers.FEED_POST_ACCOUNT_TAPPED,
            mastodonAccountId = accountId
        )
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onHashTagClicked(hashTag: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = AnalyticsIdentifiers.FEED_POST_HASHTAG_TAPPED,
            engagementValue = "hashtag: $hashTag"
        )
        navigateTo(NavigationDestination.HashTag(hashTag))
    }
}
