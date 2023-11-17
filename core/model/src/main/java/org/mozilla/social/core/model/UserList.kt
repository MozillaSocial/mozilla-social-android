package org.mozilla.social.core.model

/**
 * Represents a list of some users that the authenticated user follows.
 */
data class UserList(
    val listId: String,
    /**
     * The user-defined title of the list.
     */
    val title: String,
    /**
     * The reply policy of this list.
     */
    val replyPolicy: ListReplyPolicy,
)
