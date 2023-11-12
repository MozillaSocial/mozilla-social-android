package org.mozilla.social.core.usecase.mastodon.status

import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.usecase.mastodon.BaseUseCaseTest
import org.mozilla.social.core.test.fakes.Models
import kotlin.test.BeforeTest
import kotlin.test.Test

class UndoBoostStatusTest : BaseUseCaseTest() {

    private lateinit var subject: UndoBoostStatus

    private val networkStatus = Models.status
    private val saveStatusesToDatabase = mockk<SaveStatusesToDatabase>()

    @BeforeTest
    fun setup() {
        subject = UndoBoostStatus(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            statusRepository = statusRepository,
            socialDatabase = socialDatabase,
            dispatcherIo = testDispatcher,
        )
    }

    @Test
    fun testCancelledScope() {
        testOuterScopeCancelled(
            delayedCallBlock = {
                statusRepository.unBoostStatus(any())
            },
            delayedCallBlockReturnValue = networkStatus,
            subjectCallBlock = {
                subject("id")
            },
            verifyBlock = {
                saveStatusesToDatabase(any<List<Status>>())
            }
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                statusRepository.unBoostStatus(any())
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