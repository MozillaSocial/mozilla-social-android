package social.firefly.core.usecase.mastodon.account

import kotlinx.coroutines.test.TestScope
import social.firefly.core.usecase.mastodon.BaseUseCaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class BlockAccountTest : BaseUseCaseTest() {
    private lateinit var subject: social.firefly.core.usecase.mastodon.account.BlockAccount

    @BeforeTest
    fun setup() {
        subject =
            social.firefly.core.usecase.mastodon.account.BlockAccount(
                externalScope = TestScope(testDispatcher),
                showSnackbar = showSnackbar,
                accountRepository = accountRepository,
                relationshipRepository = relationshipRepository,
                dispatcherIo = testDispatcher,
                timelineRepository = timelineRepository,
            )
    }

    @Test
    fun testCancelledScope() {
        val accountId = "id1"
        testOuterScopeCancelled(
            delayedCallBlock = {
                relationshipRepository.updateBlocked(any(), any())
            },
            subjectCallBlock = {
                subject(accountId)
            },
            verifyBlock = {
                accountRepository.blockAccount(accountId)
            },
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                relationshipRepository.updateBlocked(any(), true)
            },
            subjectCallBlock = {
                subject("id")
            },
            verifyBlock = {
                showSnackbar(any(), any())
            },
        )
    }
}
