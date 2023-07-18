package org.mozilla.social.model.entity.request

/**
 * Push subscription
 */
data class PushSubscription(

    /**
     * Endpoint URL that is called when a notification event occurs.
     */
    val endpoint: String,

    /**
     * Push server keys.
     */
    val keys: PushKeys
)
