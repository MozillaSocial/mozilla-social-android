package org.mozilla.social.core.network.model.streaming

import org.mozilla.social.core.network.model.NetworkConversation
import org.mozilla.social.core.network.model.NetworkNotification
import org.mozilla.social.core.network.model.NetworkStatus

/**
 * A server-sent event.
 *
 * Cast the instance to any subtype to get information about the event.
 */
sealed class StreamingEvent

/**
 * A new status has appeared.
 */
data class UpdateEvent(val payload: NetworkStatus) :
    StreamingEvent()

/**
 * A new notification has appeared.
 */
data class NotificationEvent(val payload: NetworkNotification) :
    StreamingEvent()

/**
 * A conversation has been updated.
 */
data class ConversationEvent(val payload: NetworkConversation) :
    StreamingEvent()

/**
 * The status with the given [statusId] has been deleted.
 */
data class DeleteEvent(val statusId: String) :
    StreamingEvent()

/**
 * Keyword filters have been changed.
 */
object FiltersChangedEvent : StreamingEvent()
