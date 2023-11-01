package org.mozilla.social.core.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.SpyK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.mozilla.social.core.data.utils.TransactionUtils
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.dao.AccountTimelineStatusDao
import org.mozilla.social.core.database.dao.FederatedTimelineStatusDao
import org.mozilla.social.core.database.dao.HashTagTimelineStatusDao
import org.mozilla.social.core.database.dao.HomeTimelineStatusDao
import org.mozilla.social.core.database.dao.LocalTimelineStatusDao
import org.mozilla.social.core.database.dao.StatusDao
import org.mozilla.social.core.network.MediaApi
import org.mozilla.social.core.network.StatusApi
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class StatusRepositoryTest {

    @SpyK private val statusApi = mockk<StatusApi>(relaxed = true)
    @SpyK private val mediaApi = mockk<MediaApi>(relaxed = true)
    @SpyK private val socialDatabase = mockk<SocialDatabase>(relaxed = true)

    @SpyK private val statusDao = mockk<StatusDao>(relaxed = true)
    @SpyK private val homeTimelineDao = mockk<HomeTimelineStatusDao>(relaxed = true)
    @SpyK private val localTimelineDao = mockk<LocalTimelineStatusDao>(relaxed = true)
    @SpyK private val federatedTimelineDao = mockk<FederatedTimelineStatusDao>(relaxed = true)
    @SpyK private val hashTagTimelineDao = mockk<HashTagTimelineStatusDao>(relaxed = true)
    @SpyK private val accountTimelineDao = mockk<AccountTimelineStatusDao>(relaxed = true)

    private lateinit var subject: StatusRepository

    @BeforeTest
    fun setup() {
        subject = StatusRepository(
            statusApi = statusApi,
            mediaApi = mediaApi,
            socialDatabase = socialDatabase,
        )

        every { socialDatabase.statusDao() } returns statusDao
        every { socialDatabase.homeTimelineDao() } returns homeTimelineDao
        every { socialDatabase.localTimelineDao() } returns localTimelineDao
        every { socialDatabase.federatedTimelineDao() } returns federatedTimelineDao
        every { socialDatabase.hashTagTimelineDao() } returns hashTagTimelineDao
        every { socialDatabase.accountTimelineDao() } returns accountTimelineDao

        TransactionUtils.setupTransactionMock(socialDatabase)
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