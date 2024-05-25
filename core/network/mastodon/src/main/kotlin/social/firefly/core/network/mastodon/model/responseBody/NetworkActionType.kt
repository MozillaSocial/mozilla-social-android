package social.firefly.core.network.mastodon.model.responseBody

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
