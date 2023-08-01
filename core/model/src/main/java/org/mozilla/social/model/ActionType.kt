package org.mozilla.social.model

/**
 * Type of moderating action to be taken.
 */
enum class ActionType {

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
    Suspend
}
