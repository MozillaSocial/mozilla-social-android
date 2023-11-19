package org.mozilla.social.core.ui.accountfollower

import org.mozilla.social.core.model.wrappers.DetailedAccountWrapper
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickViewUiState

fun DetailedAccountWrapper.toAccountFollowerUiState(): AccountFollowerUiState =
    AccountFollowerUiState(
        accountQuickViewUiState = AccountQuickViewUiState(
            accountId = account.accountId,
            displayName = account.displayName,
            webFinger = account.acct,
            avatarUrl = account.avatarUrl,
        ),
        isFollowing = relationship.isFollowing,
        bioHtml = account.bio,
    )
