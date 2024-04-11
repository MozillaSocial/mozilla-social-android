package social.firefly.core.usecase.mastodon.status

import kotlinx.coroutines.test.TestScope
import social.firefly.core.model.Status
import social.firefly.core.test.fakes.Models
import social.firefly.core.usecase.mastodon.BaseUseCaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UndoBoostStatusTest : BaseUseCaseTest() {
    private lateinit var subject: UndoBoostStatus

    private val networkStatus = Models.status

    @BeforeTest
    fun setup() {
        subject =
            UndoBoostStatus(
                externalScope = TestScope(testDispatcher),
                databaseDelegate = databaseDelegate,
                statusRepository = statusRepository,
                showSnackbar = showSnackbar,
                dispatcherIo = testDispatcher,
                saveStatusToDatabase = saveStatusToDatabase,
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
                saveStatusToDatabase(any<Status>())
            },
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
            },
        )
    }
}
