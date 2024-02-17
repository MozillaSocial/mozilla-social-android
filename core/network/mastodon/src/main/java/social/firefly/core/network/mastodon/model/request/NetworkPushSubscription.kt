package social.firefly.core.network.mastodon.model.request

/**
 * Push subscription
 */
data class NetworkPushSubscription(
    /**
     * Endpoint URL that is called when a notification event occurs.
     */
    val endpoint: String,
    /**
     * Push server keys.
     */
    val keys: NetworkPushKeys,
)
