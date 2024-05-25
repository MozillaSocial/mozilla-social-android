package social.firefly.core.repository.mastodon.model.account

import social.firefly.core.model.Relationship
import social.firefly.core.network.mastodon.model.responseBody.NetworkRelationship

fun NetworkRelationship.toExternal(): Relationship =
    Relationship(
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
