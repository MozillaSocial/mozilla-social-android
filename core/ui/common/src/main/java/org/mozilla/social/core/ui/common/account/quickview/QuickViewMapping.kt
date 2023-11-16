package org.mozilla.social.core.ui.common.account.quickview

import org.mozilla.social.core.model.Account
import org.mozilla.social.core.model.wrappers.DetailedAccountWrapper

fun Account.toQuickViewUiState(
    isFollowing: Boolean? = null,
): AccountQuickViewUiState = AccountQuickViewUiState(
    accountId = accountId,
    displayName = displayName,
    webFinger = acct,
    avatarUrl = avatarUrl,
    isFollowing = isFollowing,
)

fun DetailedAccountWrapper.toQuickViewUiState(): AccountQuickViewUiState =
    AccountQuickViewUiState(
        accountId = account.accountId,
        displayName = account.displayName,
        webFinger = account.acct,
        avatarUrl = account.avatarUrl,
        isFollowing = relationship.isFollowing,
    )