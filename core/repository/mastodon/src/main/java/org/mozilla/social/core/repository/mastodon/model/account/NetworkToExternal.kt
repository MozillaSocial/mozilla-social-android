package org.mozilla.social.core.repository.mastodon.model.account

import org.mozilla.social.core.network.mastodon.model.NetworkRelationship
import org.mozilla.social.core.model.Relationship

fun NetworkRelationship.toExternal(): Relationship = Relationship(
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