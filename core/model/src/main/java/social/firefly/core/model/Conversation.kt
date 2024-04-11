package social.firefly.core.model

/**
 * Represents a conversation with "direct message" visibility.
 */
data class Conversation(
    val conversationId: String,
    /**
     * Participants in the conversation.
     */
    val participants: List<social.firefly.core.model.Account>,
    /**
     * Is the conversation currently marked as unread?
     */
    val isUnread: Boolean,
    /**
     * The last status in the conversation, optionally used for display.
     */
    val lastStatus: Status? = null,
)
