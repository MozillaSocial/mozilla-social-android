package org.mozilla.social.core.model

import kotlinx.datetime.Instant

/**
 * Represents a notification of an event relevant to the user.
 */
data class Notification(
    val notificationId: String,
    /**
     * The type of event that resulted in the notification.
     */
    val type: NotificationType,
    /**
     * The time of this notification.
     */
    val createdAt: Instant,
    /**
     * The account that performed the action that generated the notification.
     */
    val account: Account,
    /**
     * Status that was the object of the notification, if relevant.
     */
    val status: Status? = null,
)
