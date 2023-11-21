package org.mozilla.social.core.usecase.mastodon.account

import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatus
import org.mozilla.social.core.usecase.mastodon.BaseUseCaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class UnfollowAccountTest : BaseUseCaseTest() {
    private lateinit var subject: UnfollowAccount

    @BeforeTest
    fun setup() {
        subject =
            UnfollowAccount(
                externalScope = TestScope(testDispatcher),
                showSnackbar = showSnackbar,
                accountRepository = accountRepository,
                relationshipRepository = relationshipRepository,
                socialDatabase = socialDatabase,
                dispatcherIo = testDispatcher,
            )
    }

    @Test
    fun successTest() =
        runTest {
            val accountId = "id1"
            val loggedInId = "id2"

            subject(
                accountId = accountId,
                loggedInUserAccountId = loggedInId,
            )

            coVerify(exactly = 1) {
                homeTimelineDao.getPostsFromAccount(accountId)
                homeTimelineDao.removePostsFromAccount(accountId)
                accountRepository.updateFollowingCountInDatabase(loggedInId, -1)
                relationshipsDao.updateFollowing(accountId, false)
                accountRepository.unfollowAccount(accountId)
            }
        }

    @Test
    fun networkFailureTest() =
        runTest {
            val accountId = "id1"
            val loggedInId = "id2"

            val homeTimelinePosts =
                listOf(
                    HomeTimelineStatus(
                        statusId = "",
                        accountId = accountId,
                        pollId = null,
                        boostedStatusId = null,
                        boostedStatusAccountId = null,
                        boostedPollId = null,
                    ),
                )

            coEvery { homeTimelineDao.getPostsFromAccount(accountId) } returns homeTimelinePosts

            coEvery { accountRepository.unfollowAccount(accountId) } throws Exception()

            var exception: Exception? = null

            try {
                subject(
                    accountId = accountId,
                    loggedInUserAccountId = loggedInId,
                )
            } catch (e: Exception) {
                exception = e
            }

            assertNotNull(exception)

            coVerify(exactly = 1) {
                homeTimelineDao.getPostsFromAccount(accountId)
                homeTimelineDao.removePostsFromAccount(accountId)
                accountRepository.updateFollowingCountInDatabase(loggedInId, -1)
                relationshipsDao.updateFollowing(accountId, false)

                homeTimelineDao.insertAll(homeTimelinePosts)
                accountRepository.updateFollowingCountInDatabase(loggedInId, 1)
                relationshipsDao.updateFollowing(accountId, true)
            }
        }

    @Test
    fun outerScopeCancelledTest() {
        val accountId = "id1"
        val loggedInId = "id2"

        testOuterScopeCancelled(
            delayedCallBlock = {
                relationshipsDao.updateFollowing(any(), false)
            },
            subjectCallBlock = {
                subject(
                    accountId = accountId,
                    loggedInUserAccountId = loggedInId,
                )
            },
            verifyBlock = {
                accountRepository.unfollowAccount(accountId)
            },
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                relationshipsDao.updateFollowing(any(), false)
            },
            subjectCallBlock = {
                subject(
                    accountId = "id1",
                    loggedInUserAccountId = "id2",
                )
            },
            verifyBlock = {
                showSnackbar(any(), any())
            },
        )
    }
}
