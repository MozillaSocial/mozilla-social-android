package org.mozilla.social.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a mention of a user within the content of a status.
 */
@Serializable
data class NetworkMention(

    @SerialName("id")
    val accountId: String,

    /**
     * The username of the mentioned user.
     */
    @SerialName("username")
    val username: String,

    /**
     * The WebFinger URI of the mentioned user.
     *
     * Equivalent to [username] for local users, or username@domain for remote users.
     */
    @SerialName("acct")
    val acct: String,

    /**
     * The URL of the mentioned user's profile.
     */
    @SerialName("url")
    val url: String
)
