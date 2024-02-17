package social.firefly.core.network.mastodon.model.request

/**
 * Object to update push alert settings.
 */
data class NetworkPushUpdate(
    /**
     * Push notification settings.
     */
    val data: NetworkPushData,
)
