package org.mozilla.social.core.database.model

import androidx.room.Embedded
import kotlinx.datetime.Instant

/**
 * Represents a poll attached to a status.
 */
data class DatabasePoll(
    val pollId: String,

    /**
     * Whether the poll is currently expired.
     */
    val isExpired: Boolean,

    /**
     * Whether the poll allows multiple-choice answers.
     */
    val allowsMultipleChoices: Boolean,

    /**
     * The number of votes received on this poll.
     */
    val votesCount: Long,

    /**
     * The list of options available in this poll.
     */
    @Embedded(prefix = "option_")
    val options: List<DatabasePollOption>,

    /**
     * Custom emoji to be used for rendering poll options.
     */
    @Embedded(prefix = "emoji_")
    val emojis: List<DatabaseEmoji>,

    /**
     * Time at which the poll will expire.
     */
    val expiresAt: Instant? = null,

    /**
     * Number of unique accounts that voted on this poll.
     */
    val votersCount: Long? = null,

    /**
     * Whether the current account has voted on this poll.
     */
    val hasVoted: Boolean? = null,

    /**
     * If [hasVoted] is set, the current account's vote choices.
     */
    val ownVotes: List<Int>? = null
)
