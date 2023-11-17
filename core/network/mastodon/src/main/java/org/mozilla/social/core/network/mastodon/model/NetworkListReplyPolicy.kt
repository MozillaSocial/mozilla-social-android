package org.mozilla.social.core.network.mastodon.model

/**
 * Reply policy set on a [NetworkUserList].
 */
enum class NetworkListReplyPolicy {
    /**
     * Show replies to any followed user.
     */
    Followed,

    /**
     * Show replies to members of the list.
     */
    List,

    /**
     * Show replies to no one.
     */
    None,
}
