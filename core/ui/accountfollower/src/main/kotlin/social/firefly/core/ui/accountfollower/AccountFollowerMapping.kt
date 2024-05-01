package social.firefly.core.ui.accountfollower

import social.firefly.core.model.wrappers.DetailedAccountWrapper
import social.firefly.core.ui.common.account.quickview.AccountQuickViewUiState
import social.firefly.core.ui.common.following.FollowStatus

fun DetailedAccountWrapper.toAccountFollowerUiState(
    currentUserAccountId: String,
): AccountFollowerUiState =
    AccountFollowerUiState(
        accountQuickViewUiState = AccountQuickViewUiState(
            accountId = account.accountId,
            displayName = account.displayName,
            webFinger = account.acct,
            avatarUrl = account.avatarUrl,
        ),
        followStatus = when {
            relationship.hasPendingFollowRequest -> FollowStatus.PENDING_REQUEST
            relationship.isFollowing -> FollowStatus.FOLLOWING
            else -> FollowStatus.NOT_FOLLOWING
        },
        bioHtml = account.bio,
        followButtonVisible = currentUserAccountId != account.accountId,
    )
