package org.mozilla.social.model.entity

/**
 * Reply policy set on a [UserList].
 */
enum class ListReplyPolicy {

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
    None
}
