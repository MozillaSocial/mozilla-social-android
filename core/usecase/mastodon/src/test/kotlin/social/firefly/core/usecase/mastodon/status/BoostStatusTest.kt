package social.firefly.core.usecase.mastodon.status

import kotlinx.coroutines.test.TestScope
import social.firefly.core.model.Status
import social.firefly.core.test.fakes.Models
import social.firefly.core.usecase.mastodon.BaseUseCaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class BoostStatusTest : BaseUseCaseTest() {
    private lateinit var subject: BoostStatus

    private val networkStatus = Models.status

    @BeforeTest
    fun setup() {
        subject =
            BoostStatus(
                externalScope = TestScope(testDispatcher),
                showSnackbar = showSnackbar,
                statusRepository = statusRepository,
                dispatcherIo = testDispatcher,
                databaseDelegate = databaseDelegate,
                saveStatusToDatabase = saveStatusToDatabase,
            )
    }

    @Test
    fun testCancelledScope() {
        testOuterScopeCancelled(
            delayedCallBlock = {
                statusRepository.boostStatus(any())
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
                statusRepository.boostStatus(any())
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
