package org.mozilla.social.core.network.mastodon.model

/**
 * Type of moderating action to be taken.
 */
enum class NetworkActionType {
    /**
     * No action.
     */
    None,

    /**
     * Disable the account.
     */
    Disable,

    /**
     * Silence the account.
     */
    Silence,

    /**
     * Suspend the account.
     */
    Suspend,
}
