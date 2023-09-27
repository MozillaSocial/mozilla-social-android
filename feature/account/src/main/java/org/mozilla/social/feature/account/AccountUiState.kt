package org.mozilla.social.feature.account

data class AccountUiState(
    val accountId: String,
    val username: String,
    val webFinger: String,
    val displayName: String,
    val bio: String,
    val avatarUrl: String,
    val headerUrl: String,
    val followersCount: Long,
    val followingCount: Long,
    val statusesCount: Long,
    val fields: List<AccountFieldUiState>,
    val isBot: Boolean,
    val isFollowing: Boolean,
    val isMuted: Boolean,
    val isBlocked: Boolean,
)

data class AccountFieldUiState(
    val name: String,
    val value: String,
)