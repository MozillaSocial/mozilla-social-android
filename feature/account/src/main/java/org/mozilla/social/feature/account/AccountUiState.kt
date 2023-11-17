package org.mozilla.social.feature.account

import kotlinx.datetime.LocalDateTime

data class AccountUiState(
    val accountId: String,
    val username: String,
    val webFinger: String,
    val displayName: String,
    val accountUrl: String,
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
    val joinDate: LocalDateTime,
)

data class AccountFieldUiState(
    val name: String,
    val value: String,
)
