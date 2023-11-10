package org.mozilla.social.core.network.model.request

import org.mozilla.social.core.network.model.NetworkAlerts

/**
 * Object used to set notification alert settings.
 */
data class NetworkPushData(
    val alerts: NetworkAlerts
)
