package org.mozilla.social.core.usecase.mastodon.status

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.usecase.mastodon.BaseUseCaseTest
import org.mozilla.social.core.test.fakes.Models
import kotlin.test.BeforeTest
import kotlin.test.Test

class FavoriteStatusTest : BaseUseCaseTest() {

    private lateinit var subject: FavoriteStatus

    private val networkStatus = Models.status

    @BeforeTest
    fun setup() {
        subject = FavoriteStatus(
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
                statusRepository.favoriteStatus(any())
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
                statusRepository.favoriteStatus(any())
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