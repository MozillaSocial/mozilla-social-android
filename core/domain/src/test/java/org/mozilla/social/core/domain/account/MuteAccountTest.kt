package org.mozilla.social.core.domain.account

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.domain.BaseDomainTest
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
                accountApi.muteAccount(accountId)
            },
            subjectCallBlock = {
                subject(accountId)
            },
            verifyBlock = {
                accountApi.muteAccount(accountId)
            }
        )
    }
}