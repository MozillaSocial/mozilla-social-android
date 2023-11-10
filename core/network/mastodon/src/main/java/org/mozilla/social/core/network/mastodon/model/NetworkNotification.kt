package org.mozilla.social.core.network.mastodon.model

import kotlinx.datetime.Instant
import org.mozilla.social.core.network.mastodon.model.paging.NetworkPageable

/**
 * Represents a notification of an event relevant to the user.
 */
data class NetworkNotification(
    val notificationId: String,

    /**
     * The type of event that resulted in the notification.
     */
    val type: NetworkNotificationType,

    /**
     * The time of this notification.
     */
    val createdAt: Instant,

    /**
     * The account that performed the action that generated the notification.
     */
    val account: NetworkAccount,

    /**
     * Status that was the object of the notification, if relevant.
     */
    val status: NetworkStatus? = null
) : NetworkPageable {

    override val id: String
        get() = notificationId
}
