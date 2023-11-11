package org.mozilla.social.model

import org.mozilla.social.model.paging.Pageable

/**
 * Represents a conversation with "direct message" visibility.
 */
data class Conversation(
    val conversationId: String,

    /**
     * Participants in the conversation.
     */
    val participants: List<Account>,

    /**
     * Is the conversation currently marked as unread?
     */
    val isUnread: Boolean,

    /**
     * The last status in the conversation, optionally used for display.
     */
    val lastStatus: Status? = null
) : Pageable {

    override val id: String
        get() = conversationId
}
