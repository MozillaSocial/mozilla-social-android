package org.mozilla.social.core.ui.common.account.quickview

import org.mozilla.social.core.model.wrappers.DetailedAccountWrapper

fun DetailedAccountWrapper.toQuickViewUiState(): AccountQuickViewUiState =
    AccountQuickViewUiState(
        accountId = account.accountId,
        displayName = account.displayName,
        webFinger = account.acct,
        avatarUrl = account.avatarUrl,
        isFollowing = relationship.isFollowing,
    )