package org.mozilla.social.core.domain.status

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.domain.BaseDomainTest
import org.mozilla.social.core.network.model.NetworkPoll
import kotlin.test.BeforeTest
import kotlin.test.Test

class VoteOnPollTest : BaseDomainTest() {

    private lateinit var subject: VoteOnPoll

    private val networkPoll = NetworkPoll(
        pollId = "nostrum",
        isExpired = false,
        allowsMultipleChoices = false,
        votesCount = 8013,
        options = listOf(),
        emojis = listOf(),
        expiresAt = null,
        votersCount = null,
        hasVoted = null,
        ownVotes = listOf()
    )

    @BeforeTest
    fun setup() {
        subject = VoteOnPoll(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            statusApi = statusApi,
            socialDatabase = socialDatabase,
            dispatcherIo = testDispatcher,
        )
    }

    @Test
    fun testCancelledScope() {
        testOuterScopeCancelled(
            delayedCallBlock = {
                statusApi.voteOnPoll(any(), any())
            },
            delayedCallBlockReturnValue = networkPoll,
            subjectCallBlock = {
                subject(
                    pollId = "id",
                    pollChoices = emptyList(),
                )
            },
            verifyBlock = {
                pollsDao.update(any())
            },
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                statusApi.voteOnPoll(any(), any())
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