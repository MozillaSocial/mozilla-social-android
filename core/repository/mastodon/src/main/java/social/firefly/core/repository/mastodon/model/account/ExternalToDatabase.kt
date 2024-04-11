package social.firefly.core.repository.mastodon.model.account

import social.firefly.core.database.model.entities.DatabaseRelationship
import social.firefly.core.model.Relationship

fun Relationship.toDatabaseModel(): DatabaseRelationship =
    DatabaseRelationship(
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
