package org.mozilla.social.core.model.request

/**
 * Object used to subscribe to push notifications.
 */
data class PushSubscribe(
    /**
     * Push subscription settings.
     */
    val subscription: PushSubscription,
    /**
     * Notification alert settings.
     */
    val data: PushData,
)
