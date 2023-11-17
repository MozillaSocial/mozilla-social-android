package org.mozilla.social.core.usecase.mastodon.account

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.usecase.mastodon.BaseUseCaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class MuteAccountTest : BaseUseCaseTest() {
    private lateinit var subject: MuteAccount

    @BeforeTest
    fun setup() {
        subject =
            MuteAccount(
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
                relationshipsDao.updateMuted(any(), true)
            },
            subjectCallBlock = {
                subject(accountId)
            },
            verifyBlock = {
                accountRepository.muteAccount(accountId)
            },
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                relationshipsDao.updateMuted(any(), true)
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
