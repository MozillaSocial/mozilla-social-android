package org.mozilla.social.core.ui.common.account.quickview

import org.mozilla.social.core.database.model.accountCollections.FolloweeWrapper
import org.mozilla.social.core.database.model.accountCollections.FollowerWrapper
import org.mozilla.social.core.model.Account

fun Account.toQuickViewUiState(
    isFollowing: Boolean? = null,
): AccountQuickViewUiState = AccountQuickViewUiState(
    accountId = accountId,
    displayName = displayName,
    webFinger = acct,
    avatarUrl = avatarUrl,
    isFollowing = isFollowing,
)

fun FollowerWrapper.toQuickViewUiState(): AccountQuickViewUiState =
    AccountQuickViewUiState(
        accountId = followerAccount.accountId,
        displayName = followerAccount.displayName,
        webFinger = followerAccount.acct,
        avatarUrl = followerAccount.avatarUrl,
        isFollowing = relationship.isFollowing,
    )

fun FolloweeWrapper.toQuickViewUiState(): AccountQuickViewUiState =
    AccountQuickViewUiState(
        accountId = followingAccount.accountId,
        displayName = followingAccount.displayName,
        webFinger = followingAccount.acct,
        avatarUrl = followingAccount.avatarUrl,
        isFollowing = relationship.isFollowing,
    )