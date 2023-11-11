package org.mozilla.social.core.usecase.mastodon.account

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.usecase.mastodon.BaseDomainTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UnmuteAccountTest : BaseDomainTest() {

    private lateinit var subject: UnmuteAccount

    @BeforeTest
    fun setup() {
        subject = UnmuteAccount(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            accountApi = accountApi,
            socialDatabase = socialDatabase,
            dispatcherIo = testDispatcher,
        )
    }

    @Test
    fun testCancelledScope() {
        val accountId = "id1"
        testOuterScopeCancelled(
            delayedCallBlock = {
                relationshipsDao.updateMuted(any(), false)
            },
            subjectCallBlock = {
                subject(accountId)
            },
            verifyBlock = {
                accountApi.unmuteAccount(accountId)
            }
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                relationshipsDao.updateMuted(any(), false)
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