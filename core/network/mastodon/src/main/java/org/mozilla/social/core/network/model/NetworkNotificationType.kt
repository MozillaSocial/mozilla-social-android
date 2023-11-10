package org.mozilla.social.core.network.model

enum class NetworkNotificationType(val value: String) {

    /**
     * Someone followed the account.
     */
    Follow("follow"),

    /**
     * Someone requested to follow the account.
     */
    FollowRequest("follow_request"),

    /**
     * Someone mentioned the account in their status.
     */
    Mention("mention"),

    /**
     * Someone boosted one of the account's statuses.
     */
    Boost("reblog"),

    /**
     * Someone favourited one of the account's statuses.
     */
    Favourite("favourite"),

    /**
     * A poll the account has voted in or created has ended.
     */
    Poll("poll"),

    /**
     * Someone we enabled notifications for has posted a status.
     */
    Status("status")
}
