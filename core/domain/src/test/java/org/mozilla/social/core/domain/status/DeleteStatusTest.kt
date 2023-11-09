package org.mozilla.social.core.domain.status

import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.mozilla.social.core.domain.BaseDomainTest
import org.mozilla.social.core.domain.utils.TestModels
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class DeleteStatusTest : BaseDomainTest() {

    private lateinit var subject: DeleteStatus

    private val networkStatus = TestModels.networkStatus

    @BeforeTest
    fun setup() {
        subject = DeleteStatus(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            statusApi = statusApi,
            socialDatabase = socialDatabase,
            dispatcherIo = testDispatcher,
        )
    }

    @Test
    fun successTest() = runTest {
        subject("id")

        coVerify(exactly = 1) {
            statusDao.updateIsBeingDeleted("id", true)
            statusApi.deleteStatus("id")
            homeTimelineDao.deletePost("id")
            localTimelineDao.deletePost("id")
            federatedTimelineDao.deletePost("id")
            hashTagTimelineDao.deletePost("id")
            accountTimelineDao.deletePost("id")
            statusDao.deleteStatus("id")
        }
    }

    @Test
    fun networkFailureTest() = runTest {
        coEvery { statusApi.deleteStatus("id") } throws Exception()
        var exception: Exception? = null
        try {
            subject("id")
        } catch (e: Exception) {
            exception = e
        }

        assertNotNull(exception)

        coVerify(exactly = 1) {
            statusDao.updateIsBeingDeleted("id", true)
            statusApi.deleteStatus("id")
            statusDao.updateIsBeingDeleted("id", false)
        }

        coVerify(exactly = 0) {
            homeTimelineDao.deletePost("id")
            localTimelineDao.deletePost("id")
            federatedTimelineDao.deletePost("id")
            hashTagTimelineDao.deletePost("id")
            accountTimelineDao.deletePost("id")
            statusDao.deleteStatus("id")
        }
    }

    @Test
    fun testCancelledScope() {
        testOuterScopeCancelled(
            delayedCallBlock = {
                statusApi.deleteStatus(any())
            },
            delayedCallBlockReturnValue = networkStatus,
            subjectCallBlock = {
                subject("id")
            },
            verifyBlock = {
                statusDao.deleteStatus(any())
            }
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                statusApi.deleteStatus(any())
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