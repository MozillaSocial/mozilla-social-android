package org.mozilla.social.core.usecase.mastodon.status

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.test.fakes.Models
import org.mozilla.social.core.usecase.mastodon.BaseUseCaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UndoFavoriteStatusTest : BaseUseCaseTest() {
    private lateinit var subject: UndoFavoriteStatus

    private val networkStatus = Models.status

    @BeforeTest
    fun setup() {
        subject =
            UndoFavoriteStatus(
                externalScope = TestScope(testDispatcher),
                showSnackbar = showSnackbar,
                statusRepository = statusRepository,
                databaseDelegate = databaseDelegate,
                dispatcherIo = testDispatcher,
                saveStatusToDatabase = saveStatusToDatabase,
            )
    }

    @Test
    fun testCancelledScope() {
        testOuterScopeCancelled(
            delayedCallBlock = {
                statusRepository.unFavoriteStatus(any())
            },
            delayedCallBlockReturnValue = networkStatus,
            subjectCallBlock = {
                subject("id")
            },
            verifyBlock = {
                saveStatusToDatabase(any<Status>())
            },
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                statusRepository.unFavoriteStatus(any())
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
