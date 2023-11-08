package org.mozilla.social.core.ui.poll

import kotlin.test.Test
import org.mozilla.social.model.Poll
import org.mozilla.social.model.PollOption
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PollMappingTest {

    private val basePoll = Poll(
        pollId = "",
        isExpired = false,
        allowsMultipleChoices = false,
        votesCount = 3,
        options = listOf(
            PollOption(
                title = "first",
                votesCount = 1,
            ),
            PollOption(
                title = "second",
                votesCount = 2,
            ),
        ),
        emojis = emptyList(),
        expiresAt = null,
        votersCount = 3,
        hasVoted = true,
        ownVotes = listOf()
    )

    @Test
    fun `Results show`() {
        assertTrue(basePoll.toPollUiState(true).showResults)
    }

    @Test
    fun `Results are hidden when the option counts are null`() {
        val poll = basePoll.copy(
            options = listOf(
                PollOption(
                    title = "first",
                    votesCount = null,
                ),
                PollOption(
                    title = "second",
                    votesCount = null,
                ),
            )
        )

        assertFalse(poll.toPollUiState(true).showResults)
    }

    @Test
    fun `Results are hidden when user has not voted`() {
        val poll = basePoll.copy(
            hasVoted = false,
        )

        assertFalse(poll.toPollUiState(true).showResults)
    }

    @Test
    fun `Results are shown when user has not voted but poll is expired`() {
        val poll = basePoll.copy(
            hasVoted = false,
            isExpired = true,
        )

        assertTrue(poll.toPollUiState(true).showResults)
    }

    @Test
    fun `Results are hidden when user has not voted but poll is expired but options are null for some reason`() {
        val poll = basePoll.copy(
            hasVoted = false,
            isExpired = true,
            options = listOf(
                PollOption(
                    title = "first",
                    votesCount = null,
                ),
                PollOption(
                    title = "second",
                    votesCount = null,
                ),
            )
        )

        assertFalse(poll.toPollUiState(true).showResults)
    }
}