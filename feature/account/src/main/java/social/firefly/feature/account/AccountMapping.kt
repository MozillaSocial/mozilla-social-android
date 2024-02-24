package social.firefly.feature.account

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import social.firefly.core.model.Account
import social.firefly.core.model.Field
import social.firefly.core.model.Relationship
import social.firefly.core.ui.common.following.FollowStatus

fun Account.toUiState(relationship: Relationship) =
    AccountUiState(
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
        followStatus = when {
            relationship.hasPendingFollowRequest -> FollowStatus.PENDING_REQUEST
            relationship.isFollowing -> FollowStatus.FOLLOWING
            else -> FollowStatus.NOT_FOLLOWING
        },
        isMuted = relationship.isMuting,
        isBlocked = relationship.isBlocking,
        joinDate = createdAt.toLocalDateTime(TimeZone.currentSystemDefault()),
    )

fun Field.toUiState() =
    AccountFieldUiState(
        name = name,
        value = value,
    )
