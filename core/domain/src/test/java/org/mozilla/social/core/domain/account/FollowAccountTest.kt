package org.mozilla.social.core.domain.account

import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mozilla.social.core.domain.BaseDomainTest
import kotlin.test.BeforeTest
import kotlin.test.assertNotNull

class FollowAccountTest : BaseDomainTest() {

    private lateinit var subject: FollowAccount

    @BeforeTest
    fun setup() {
        subject = FollowAccount(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            accountApi = accountApi,
            socialDatabase = socialDatabase,
            dispatcherIo = testDispatcher,
        )
    }

    @Test
    fun successTest() = runTest {
        val accountId = "id1"
        val loggedInId = "id2"
        subject(
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
    fun networkFailureTest() = runTest {
        val accountId = "id1"
        val loggedInId = "id2"

        coEvery { accountApi.followAccount(accountId) } throws Exception()

        var exception: Exception? = null

        try {
            subject(
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
}