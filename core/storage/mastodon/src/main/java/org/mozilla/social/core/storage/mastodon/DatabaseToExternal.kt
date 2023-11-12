package org.mozilla.social.core.storage.mastodon

import org.mozilla.social.core.database.model.DatabaseRelationship
import org.mozilla.social.core.model.Relationship

fun DatabaseRelationship.toExternal(): Relationship = Relationship(
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