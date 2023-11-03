package org.mozilla.social.core.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class StatusRepositoryTest : BaseRepositoryTest() {

    private lateinit var subject: StatusRepository

    @BeforeTest
    fun setup() {
        subject = StatusRepository(
            statusApi = statusApi,
            mediaApi = mediaApi,
            socialDatabase = socialDatabase,
        )
    }

    @Test
    fun deleteStatusTest() = runTest {
        subject.deleteStatus("id")

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
    fun deleteStatusNetworkFailureTest() = runTest {
        coEvery { statusApi.deleteStatus("id") } throws Exception()
        var exception: Exception? = null
        try {
            subject.deleteStatus("id")
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
}