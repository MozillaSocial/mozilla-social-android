package social.firefly.core.ui.accountfollower

import social.firefly.core.model.wrappers.DetailedAccountWrapper
import social.firefly.core.ui.common.account.quickview.AccountQuickViewUiState

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
        isFollowing = relationship.isFollowing,
        bioHtml = account.bio,
        followButtonVisible = currentUserAccountId != account.accountId,
    )
