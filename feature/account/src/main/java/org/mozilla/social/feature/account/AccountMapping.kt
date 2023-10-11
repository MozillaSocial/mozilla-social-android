package org.mozilla.social.feature.account

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.mozilla.social.model.Account
import org.mozilla.social.model.Field
import org.mozilla.social.model.Relationship

fun Account.toUiState(
    relationship: Relationship,
) = AccountUiState(
    accountId = accountId,
    username = username,
    webFinger = acct,
    displayName = displayName,
    accountUrl = url,
    bio = bio,
    avatarUrl = avatarUrl,
    headerUrl = headerUrl,
    followersCount = followersCount,
    followingCount = followingCount,
    statusesCount = statusesCount,
    fields = fields?.map { it.toUiState() } ?: emptyList(),
    isBot = isBot ?: false,
    isFollowing = relationship.isFollowing,
    isMuted = relationship.isMuting,
    isBlocked = relationship.isBlocking,
    joinDate = createdAt.toLocalDateTime(TimeZone.currentSystemDefault()),
)

fun Field.toUiState() = AccountFieldUiState(
    name = name,
    value = value,
)