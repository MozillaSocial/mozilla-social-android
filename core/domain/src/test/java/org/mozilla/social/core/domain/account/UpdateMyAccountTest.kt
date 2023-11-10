package org.mozilla.social.core.domain.account

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.domain.BaseDomainTest
import org.mozilla.social.core.test.fakes.NetworkModels
import kotlin.test.BeforeTest
import kotlin.test.Test

class UpdateMyAccountTest : BaseDomainTest() {

    private lateinit var subject: UpdateMyAccount

    private val networkAccountMock = NetworkModels.networkAccount

    @BeforeTest
    fun setup() {
        subject = UpdateMyAccount(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            accountApi = accountApi,
            socialDatabase = socialDatabase,
            dispatcherIo = testDispatcher,
        )
    }

    @Test
    fun testCancelledScope() {
        testOuterScopeCancelled(
            delayedCallBlock = {
                accountApi.updateAccount(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            },
            delayedCallBlockReturnValue = networkAccountMock,
            subjectCallBlock = {
                subject()
            },
            verifyBlock = {
                accountsDao.insert(any())
            }
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                accountApi.updateAccount(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            },
            subjectCallBlock = {
                subject()
            },
            verifyBlock = {
                showSnackbar(any(), any())
            },
        )
    }
}