package org.mozilla.social.core.usecase.mastodon.account

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.usecase.mastodon.BaseUseCaseTest
import org.mozilla.social.core.test.fakes.Models
import kotlin.test.BeforeTest
import kotlin.test.Test

class UpdateMyAccountTest : BaseUseCaseTest() {

    private lateinit var subject: UpdateMyAccount

    private val networkAccountMock = Models.networkAccount

    @BeforeTest
    fun setup() {
        subject = UpdateMyAccount(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            accountRepository = accountRepository,
            localAccountRepository = localAccountRepository,
            dispatcherIo = testDispatcher,
        )
    }

    @Test
    fun testCancelledScope() {
        testOuterScopeCancelled(
            delayedCallBlock = {
                accountRepository.updateAccount(
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
                localAccountRepository.insertAccount(any())
            }
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                accountRepository.updateAccount(
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