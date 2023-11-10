package org.mozilla.social.core.network.mastodon.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a poll attached to a status.
 */
@Serializable
data class NetworkPoll(

    @SerialName("id")
    val pollId: String,

    /**
     * Whether the poll is currently expired.
     */
    @SerialName("expired")
    val isExpired: Boolean,

    /**
     * Whether the poll allows multiple-choice answers.
     */
    @SerialName("multiple")
    val allowsMultipleChoices: Boolean,

    /**
     * The number of votes received on this poll.
     */
    @SerialName("votes_count")
    val votesCount: Long,

    /**
     * The list of options available in this poll.
     */
    @SerialName("options")
    val options: List<NetworkPollOption>,

    /**
     * Custom emoji to be used for rendering poll options.
     */
    @SerialName("emojis")
    val emojis: List<NetworkEmoji>,

    /**
     * Time at which the poll will expire.
     */
    @SerialName("expires_at")
    val expiresAt: Instant? = null,

    /**
     * Number of unique accounts that voted on this poll.
     */
    @SerialName("voters_count")
    val votersCount: Long? = null,

    /**
     * Whether the current account has voted on this poll.
     */
    @SerialName("voted")
    val hasVoted: Boolean? = null,

    /**
     * If [hasVoted] is set, the current account's vote choices.
     */
    @SerialName("own_votes")
    val ownVotes: List<Int>? = null
)
