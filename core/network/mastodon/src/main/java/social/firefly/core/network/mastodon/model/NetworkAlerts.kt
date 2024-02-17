package social.firefly.core.network.mastodon.model

/**
 * Used to select which alerts to receive from a [NetworkPushSubscription].
 */
data class NetworkAlerts(
    /**
     * Receive follow notifications?
     */
    val onFollow: Boolean,
    /**
     * Receive favourite notifications?
     */
    val onFavourite: Boolean,
    /**
     * Receive mention notifications?
     */
    val onMention: Boolean,
    /**
     * Receive boost notifications?
     */
    val onBoost: Boolean,
    /**
     * Receive poll notifications?
     */
    val onPoll: Boolean,
)
