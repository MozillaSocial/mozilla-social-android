package org.mozilla.social.model

/**
 * Represents the relationship between accounts.
 */
data class Relationship(

    /**
     * ID of the account being examined.
     */
    val accountId: String,

    /**
     * Whether the current account is following this account.
     */
    val isFollowing: Boolean,

    /**
     * Whether the current account has a pending follow request for this account.
     */
    val hasPendingFollowRequest: Boolean,

    /**
     * Whether the current account is featuring this account on their profile.
     */
    val isFeatured: Boolean,

    /**
     * Whether the current account is being followed by this account.
     */
    val isFollowedBy: Boolean,

    /**
     * Whether the current account has muted this account.
     */
    val isMuting: Boolean,

    /**
     * Whether the current account has muted notifications for this account.
     */
    val isMutingNotifications: Boolean,

    /**
     * Whether the current account is showing boosts from this account.
     */
    val isShowingBoosts: Boolean,

    /**
     * Whether the current account has enabled notifications for this account.
     */
    val isNotifying: Boolean,

    /**
     * Whether the current account is blocking this account.
     */
    val isBlocking: Boolean,

    /**
     * Whether the current account is blocking this account's domain.
     */
    val isDomainBlocking: Boolean,

    /**
     * Whether the current account is being blocked by this account.
     */
    val isBlockedBy: Boolean,

    /**
     * A private comment left by the current account on this account.
     */
    val note: String
)
