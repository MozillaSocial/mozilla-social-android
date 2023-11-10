package org.mozilla.social.core.network.mastodon.model.request

import org.mozilla.social.core.network.mastodon.model.NetworkAlerts

/**
 * Object used to set notification alert settings.
 */
data class NetworkPushData(
    val alerts: NetworkAlerts
)
