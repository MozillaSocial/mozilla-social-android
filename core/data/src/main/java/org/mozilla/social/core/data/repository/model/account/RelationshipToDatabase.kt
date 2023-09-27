package org.mozilla.social.core.data.repository.model.account

import org.mozilla.social.core.database.model.DatabaseRelationship
import org.mozilla.social.model.Relationship

fun Relationship.toDatabaseModel(): DatabaseRelationship = DatabaseRelationship(
    accountId = accountId,
    isFollowing = isFollowing,
    hasPendingFollowRequest = hasPendingFollowRequest,
    isFollowedBy = isFollowedBy,
    isMuting = isMuting,
    isMutingNotifications = isMutingNotifications,
    isShowingBoosts = isShowingBoosts,
    isNotifying = isNotifying,
    isBlocking = isBlocking,
    isDomainBlocking = isDomainBlocking,
    isBlockedBy = isBlockedBy,
    endorsed = endorsed,
)