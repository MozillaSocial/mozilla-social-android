package org.mozilla.social.core.usecase.mastodon.status

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.model.Poll
import org.mozilla.social.core.usecase.mastodon.BaseUseCaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class VoteOnPollTest : BaseUseCaseTest() {
    private lateinit var subject: VoteOnPoll

    private val networkPoll =
        Poll(
            pollId = "nostrum",
            isExpired = false,
            allowsMultipleChoices = false,
            votesCount = 8013,
            options = listOf(),
            emojis = listOf(),
            expiresAt = null,
            votersCount = null,
            hasVoted = null,
            ownVotes = listOf(),
        )

    @BeforeTest
    fun setup() {
        subject =
            VoteOnPoll(
                externalScope = TestScope(testDispatcher),
                showSnackbar = showSnackbar,
                statusRepository = statusRepository,
                pollRepository = pollRepository,
                dispatcherIo = testDispatcher,
            )
    }

    @Test
    fun testCancelledScope() {
        testOuterScopeCancelled(
            delayedCallBlock = {
                statusRepository.voteOnPoll(any(), any())
            },
            delayedCallBlockReturnValue = networkPoll,
            subjectCallBlock = {
                subject(
                    pollId = "id",
                    pollChoices = emptyList(),
                )
            },
            verifyBlock = {
                pollRepository.insert(any())
            },
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                statusRepository.voteOnPoll(any(), any())
            },
            subjectCallBlock = {
                subject(
                    pollId = "id",
                    pollChoices = emptyList(),
                )
            },
            verifyBlock = {
                showSnackbar(any(), any())
            },
        )
    }
}
