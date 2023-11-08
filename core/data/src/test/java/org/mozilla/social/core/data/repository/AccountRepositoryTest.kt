package org.mozilla.social.core.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatus
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class AccountRepositoryTest : BaseRepositoryTest() {

    private lateinit var subject: AccountRepository

    private val dispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setup() {
        subject = AccountRepository(
            accountApi = accountApi,
            socialDatabase = socialDatabase,
            externalScope = TestScope(dispatcher),
            dispatcherIo = dispatcher
        )
    }

    @Test
    fun followAccountTest() = runTest {
        val accountId = "id1"
        val loggedInId = "id2"
        subject.followAccount(
            accountId = accountId,
            loggedInUserAccountId = loggedInId
        )

        coVerify(exactly = 1) {
            accountsDao.updateFollowingCount(loggedInId, 1)
            relationshipsDao.updateFollowing(accountId, true)
            accountApi.followAccount(accountId)
        }
    }

    @Test
    fun followAccountNetworkFailureTest() = runTest {
        val accountId = "id1"
        val loggedInId = "id2"

        coEvery { accountApi.followAccount(accountId) } throws Exception()

        var exception: Exception? = null

        try {
            subject.followAccount(
                accountId = accountId,
                loggedInUserAccountId = loggedInId
            )
        } catch (e: Exception) {
            exception = e
        }

        assertNotNull(exception)

        coVerify(exactly = 1) {
            accountsDao.updateFollowingCount(loggedInId, 1)
            relationshipsDao.updateFollowing(accountId, true)
            accountsDao.updateFollowingCount(loggedInId, -1)
            relationshipsDao.updateFollowing(accountId, false)
        }
    }

    @Test
    fun unfollowAccountTest() = runTest {
        val accountId = "id1"
        val loggedInId = "id2"

        subject.unfollowAccount(
            accountId = accountId,
            loggedInUserAccountId = loggedInId
        )

        coVerify(exactly = 1) {
            homeTimelineDao.getPostsFromAccount(accountId)
            homeTimelineDao.removePostsFromAccount(accountId)
            accountsDao.updateFollowingCount(loggedInId, -1)
            relationshipsDao.updateFollowing(accountId, false)
            accountApi.unfollowAccount(accountId)
        }
    }

    @Test
    fun unfollowAccountNetworkFailureTest() = runTest {
        val accountId = "id1"
        val loggedInId = "id2"

        val homeTimelinePosts = listOf(
            HomeTimelineStatus(
                statusId = "",
                accountId = accountId,
                pollId = null,
                boostedStatusId = null,
                boostedStatusAccountId = null,
                boostedPollId = null,
            )
        )

        coEvery { homeTimelineDao.getPostsFromAccount(accountId) } returns homeTimelinePosts

        coEvery { accountApi.unfollowAccount(accountId) } throws Exception()

        var exception: Exception? = null

        try {
            subject.unfollowAccount(
                accountId = accountId,
                loggedInUserAccountId = loggedInId
            )
        } catch (e: Exception) {
            exception = e
        }

        assertNotNull(exception)

        coVerify(exactly = 1) {
            homeTimelineDao.getPostsFromAccount(accountId)
            homeTimelineDao.removePostsFromAccount(accountId)
            accountsDao.updateFollowingCount(loggedInId, -1)
            relationshipsDao.updateFollowing(accountId, false)

            homeTimelineDao.insertAll(homeTimelinePosts)
            accountsDao.updateFollowingCount(loggedInId, 1)
            relationshipsDao.updateFollowing(accountId, true)
        }
    }
}