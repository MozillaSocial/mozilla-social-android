package org.mozilla.social.core.domain.report

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.domain.BaseDomainTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ReportTest : BaseDomainTest() {

    private lateinit var subject: Report

    @BeforeTest
    fun setup() {
        subject = Report(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            reportApi = reportApi,
            dispatcherIo = testDispatcher,
        )
    }

    @Test
    fun testCancelledScope() {
        val accountId = "id1"
        testOuterScopeCancelled(
            delayedCallBlock = {
                reportApi.report(any())
            },
            subjectCallBlock = {
                subject(accountId)
            },
            verifyBlock = {
                reportApi.report(any())
            }
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                reportApi.report(any())
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