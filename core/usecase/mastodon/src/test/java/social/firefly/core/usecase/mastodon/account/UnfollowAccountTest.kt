package social.firefly.core.usecase.mastodon.account

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import social.firefly.core.model.Status
import social.firefly.core.usecase.mastodon.BaseUseCaseTest
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
                databaseDelegate = databaseDelegate,
                dispatcherIo = testDispatcher,
                timelineRepository = timelineRepository,
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
                timelineRepository.getPostsFromHomeTimelineForAccount(accountId)
                timelineRepository.removePostInHomeTimelineForAccount(accountId)
                accountRepository.updateFollowingCountInDatabase(loggedInId, -1)
                relationshipRepository.updateFollowing(accountId, false)
                accountRepository.unfollowAccount(accountId)
            }
        }

    @Test
    fun networkFailureTest() =
        runTest {
            val homeTimelinePosts = mockk<List<Status>>()
            val accountId = "id1"
            val loggedInId = "id2"

            coEvery { timelineRepository.getPostsFromHomeTimelineForAccount(accountId) } returns homeTimelinePosts

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
                timelineRepository.getPostsFromHomeTimelineForAccount(accountId)
                timelineRepository.removePostInHomeTimelineForAccount(accountId)
                accountRepository.updateFollowingCountInDatabase(loggedInId, -1)
                relationshipRepository.updateFollowing(accountId, false)

                timelineRepository.insertAllIntoHomeTimeline(homeTimelinePosts)
                accountRepository.updateFollowingCountInDatabase(loggedInId, 1)
                relationshipRepository.updateFollowing(accountId, true)
            }
        }

    @Test
    fun outerScopeCancelledTest() {
        val accountId = "id1"
        val loggedInId = "id2"

        testOuterScopeCancelled(
            delayedCallBlock = {
                relationshipRepository.updateFollowing(any(), false)
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
                relationshipRepository.updateFollowing(any(), false)
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
