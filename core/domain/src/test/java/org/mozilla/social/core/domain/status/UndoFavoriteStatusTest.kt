package org.mozilla.social.core.domain.status

import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.domain.BaseDomainTest
import org.mozilla.social.core.domain.utils.TestModels
import kotlin.test.BeforeTest
import kotlin.test.Test

class UndoFavoriteStatusTest : BaseDomainTest() {

    private val statusRepository = mockk<StatusRepository>(relaxed = true)

    private lateinit var subject: UndoFavoriteStatus

    private val networkStatus = TestModels.networkStatus

    @BeforeTest
    fun setup() {
        subject = UndoFavoriteStatus(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            statusApi = statusApi,
            statusRepository = statusRepository,
            socialDatabase = socialDatabase,
            dispatcherIo = testDispatcher,
        )
    }

    @Test
    fun testCancelledScope() {
        testOuterScopeCancelled(
            delayedCallBlock = {
                statusApi.unFavoriteStatus(any())
            },
            delayedCallBlockReturnValue = networkStatus,
            subjectCallBlock = {
                subject("id")
            },
            verifyBlock = {
                statusRepository.saveStatusToDatabase(any())
            }
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                statusApi.unFavoriteStatus(any())
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