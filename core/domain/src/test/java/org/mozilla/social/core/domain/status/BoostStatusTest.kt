package org.mozilla.social.core.domain.status

import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.domain.BaseDomainTest
import org.mozilla.social.core.test.fakes.NetworkModels
import kotlin.test.BeforeTest
import kotlin.test.Test

class BoostStatusTest : BaseDomainTest() {

    private val statusRepository = mockk<StatusRepository>(relaxed = true)

    private lateinit var subject: BoostStatus

    private val networkStatus = NetworkModels.networkStatus

    @BeforeTest
    fun setup() {
        subject = BoostStatus(
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
                statusApi.boostStatus(any())
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
                statusApi.boostStatus(any())
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