package org.mozilla.social.core.network.mastodon.model

import org.mozilla.social.core.network.mastodon.model.paging.NetworkPageable

/**
 * Represents a conversation with "direct message" visibility.
 */
data class NetworkConversation(
    val conversationId: String,
    /**
     * Participants in the conversation.
     */
    val participants: List<NetworkAccount>,
    /**
     * Is the conversation currently marked as unread?
     */
    val isUnread: Boolean,
    /**
     * The last status in the conversation, optionally used for display.
     */
    val lastStatus: NetworkStatus? = null,
) : NetworkPageable {
    override val id: String
        get() = conversationId
}
