package org.mozilla.social.core.domain.account

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.domain.BaseDomainTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UnblockAccountTest: BaseDomainTest() {

    private lateinit var subject: UnblockAccount

    @BeforeTest
    fun setup() {
        subject = UnblockAccount(
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
                relationshipsDao.updateBlocked(any(), false)
            },
            subjectCallBlock = {
                subject(accountId)
            },
            verifyBlock = {
                accountApi.unblockAccount(accountId)
            }
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