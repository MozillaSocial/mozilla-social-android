package org.mozilla.social.model

/**
 * Used to select which alerts to receive from a [PushSubscription].
 */
data class Alerts(

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
    val onPoll: Boolean
)
