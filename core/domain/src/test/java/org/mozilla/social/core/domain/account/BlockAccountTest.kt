package org.mozilla.social.core.domain.account

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.domain.BaseDomainTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class BlockAccountTest : BaseDomainTest() {

    private lateinit var subject: BlockAccount

    @BeforeTest
    fun setup() {
        subject = BlockAccount(
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
                accountApi.blockAccount(accountId)
            },
            subjectCallBlock = {
                subject(accountId)
            },
            verifyBlock = {
                accountApi.blockAccount(accountId)
            }
        )
    }
}