package social.firefly.core.network.mastodon.model.responseBody

/**
 * Represents a list of some users that the authenticated user follows.
 */
data class NetworkUserList(
    val listId: String,
    /**
     * The user-defined title of the list.
     */
    val title: String,
    /**
     * The reply policy of this list.
     */
    val replyPolicy: social.firefly.core.network.mastodon.model.responseBody.NetworkListReplyPolicy,
)
