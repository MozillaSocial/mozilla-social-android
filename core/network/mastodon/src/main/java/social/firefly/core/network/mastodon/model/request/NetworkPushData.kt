package social.firefly.core.network.mastodon.model.request

import social.firefly.core.network.mastodon.model.NetworkAlerts

/**
 * Object used to set notification alert settings.
 */
data class NetworkPushData(
    val alerts: NetworkAlerts,
)
