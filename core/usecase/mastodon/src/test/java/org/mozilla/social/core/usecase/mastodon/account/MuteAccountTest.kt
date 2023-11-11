package org.mozilla.social.core.usecase.mastodon.account

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.usecase.mastodon.BaseDomainTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class MuteAccountTest : BaseDomainTest() {

    private lateinit var subject: MuteAccount

    @BeforeTest
    fun setup() {
        subject = MuteAccount(
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
                relationshipsDao.updateMuted(any(), true)
            },
            subjectCallBlock = {
                subject(accountId)
            },
            verifyBlock = {
                accountApi.muteAccount(accountId)
            }
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