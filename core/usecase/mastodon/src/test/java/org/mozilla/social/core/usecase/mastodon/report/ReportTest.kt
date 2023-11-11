package org.mozilla.social.core.usecase.mastodon.report

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.usecase.mastodon.BaseUseCaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ReportTest : BaseUseCaseTest() {

    private lateinit var subject: Report

    @BeforeTest
    fun setup() {
        subject = Report(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            reportRepository = reportRepository,
            dispatcherIo = testDispatcher,
        )
    }

    @Test
    fun testCancelledScope() {
        val accountId = "id1"
        testOuterScopeCancelled(
            delayedCallBlock = {
                reportRepository.report(any())
            },
            subjectCallBlock = {
                subject(accountId)
            },
            verifyBlock = {
                reportRepository.report(any())
            }
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                reportRepository.report(any())
            },
            subjectCallBlock = {
                subject("id")
            },
            verifyBlock = {
                showSnackbar(any(), any())
            }
        )
    }
}