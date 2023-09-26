package org.mozilla.social.feature.account

import org.mozilla.social.model.Account
import org.mozilla.social.model.Field
import org.mozilla.social.model.Relationship

fun Account.toUiState(
    relationship: Relationship,
) = AccountUiState(
    accountId = accountId,
    username = username,
    webFinger = acct,
    bio = bio,
    avatarUrl = avatarUrl,
    followersCount = followersCount,
    followingCount = followingCount,
    fields = fields?.map { it.toUiState() } ?: emptyList(),
    isBot = isBot ?: false,
    isFollowing = relationship.isFollowing,
    isMuted = relationship.isMuting,
    isBlocked = relationship.isBlocking
)

fun Field.toUiState() = AccountFieldUiState(
    name = name,
    value = value,
)