package org.mozilla.social.core.usecase.mastodon.status

import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.usecase.mastodon.BaseDomainTest
import org.mozilla.social.core.test.fakes.NetworkModels
import kotlin.test.BeforeTest
import kotlin.test.Test

class FavoriteStatusTest : BaseDomainTest() {

    private val statusRepository = mockk<StatusRepository>(relaxed = true)

    private lateinit var subject: FavoriteStatus

    private val networkStatus = NetworkModels.networkStatus

    @BeforeTest
    fun setup() {
        subject = FavoriteStatus(
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
                statusApi.favoriteStatus(any())
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
                statusApi.favoriteStatus(any())
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