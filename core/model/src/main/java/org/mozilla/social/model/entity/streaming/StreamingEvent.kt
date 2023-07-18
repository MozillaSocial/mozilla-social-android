package org.mozilla.social.model.entity.streaming

import org.mozilla.social.model.entity.Conversation
import org.mozilla.social.model.entity.Notification
import org.mozilla.social.model.entity.Status

/**
 * A server-sent event.
 *
 * Cast the instance to any subtype to get information about the event.
 */
sealed class StreamingEvent

/**
 * A new status has appeared.
 */
data class UpdateEvent(val payload: Status) : StreamingEvent()

/**
 * A new notification has appeared.
 */
data class NotificationEvent(val payload: Notification) : StreamingEvent()

/**
 * A conversation has been updated.
 */
data class ConversationEvent(val payload: Conversation) : StreamingEvent()

/**
 * The status with the given [statusId] has been deleted.
 */
data class DeleteEvent(val statusId: String) : StreamingEvent()

/**
 * Keyword filters have been changed.
 */
object FiltersChangedEvent : StreamingEvent()
