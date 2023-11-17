package org.mozilla.social.core.model

/**
 * Represents a subscription to the push streaming server.
 */
data class PushSubscription(
    val subscriptionId: String,
    /**
     * URL where push alerts will be sent to.
     */
    val endpoint: String,
    /**
     * The streaming server's VAPID key.
     */
    val serverKey: String,
    /**
     * Which alerts should be delivered to the endpoint.
     */
    val alerts: Alerts,
)
