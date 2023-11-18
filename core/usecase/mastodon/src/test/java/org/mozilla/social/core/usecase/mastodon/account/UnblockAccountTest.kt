package org.mozilla.social.core.usecase.mastodon.account

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.usecase.mastodon.BaseUseCaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UnblockAccountTest : BaseUseCaseTest() {
    private lateinit var subject: UnblockAccount

    @BeforeTest
    fun setup() {
        subject =
            UnblockAccount(
                externalScope = TestScope(testDispatcher),
                showSnackbar = showSnackbar,
                accountRepository = accountRepository,
                socialDatabase = socialDatabase,
                dispatcherIo = testDispatcher,
            )
    }

    @Test
    fun testCancelledScope() {
        val accountId = "id1"
        testOuterScopeCancelled(
            delayedCallBlock = {
                relationshipsDao.updateBlocked(any(), false)
            },
            subjectCallBlock = {
                subject(accountId)
            },
            verifyBlock = {
                accountRepository.unblockAccount(accountId)
            },
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                relationshipsDao.updateBlocked(any(), false)
            },
            subjectCallBlock = {
                subject("id1")
            },
            verifyBlock = {
                showSnackbar(any(), any())
            },
        )
    }
}
