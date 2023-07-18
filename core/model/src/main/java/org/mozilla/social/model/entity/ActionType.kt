package org.mozilla.social.model.entity

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
