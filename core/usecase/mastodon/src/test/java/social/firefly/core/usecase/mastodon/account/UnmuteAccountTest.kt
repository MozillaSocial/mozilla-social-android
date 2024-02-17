package social.firefly.core.usecase.mastodon.account

import kotlinx.coroutines.test.TestScope
import social.firefly.core.usecase.mastodon.BaseUseCaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UnmuteAccountTest : BaseUseCaseTest() {
    private lateinit var subject: UnmuteAccount

    @BeforeTest
    fun setup() {
        subject =
            UnmuteAccount(
                externalScope = TestScope(testDispatcher),
                showSnackbar = showSnackbar,
                accountRepository = accountRepository,
                relationshipRepository = relationshipRepository,
                dispatcherIo = testDispatcher,
            )
    }

    @Test
    fun testCancelledScope() {
        val accountId = "id1"
        testOuterScopeCancelled(
            delayedCallBlock = {
                relationshipRepository.updateMuted(any(), false)
            },
            subjectCallBlock = {
                subject(accountId)
            },
            verifyBlock = {
                accountRepository.unmuteAccount(accountId)
            },
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                relationshipRepository.updateMuted(any(), false)
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
