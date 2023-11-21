package org.mozilla.social.core.usecase.mastodon.account

import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.mozilla.social.core.usecase.mastodon.BaseUseCaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class FollowAccountTest : BaseUseCaseTest() {
    private lateinit var subject: FollowAccount

    @BeforeTest
    fun setup() {
        subject =
            FollowAccount(
                externalScope = TestScope(testDispatcher),
                showSnackbar = showSnackbar,
                accountRepository = accountRepository,
                relationshipRepository = relationshipRepository,
                socialDatabase = databaseDelegate,
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
                accountRepository.updateFollowingCountInDatabase(loggedInId, 1)
                relationshipRepository.updateFollowing(accountId, true)
                accountRepository.followAccount(accountId)
            }
        }

    @Test
    fun networkFailureTest() =
        runTest {
            val accountId = "id1"
            val loggedInId = "id2"

            coEvery { accountRepository.followAccount(accountId) } throws Exception()

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
                accountRepository.updateFollowingCountInDatabase(loggedInId, 1)
                relationshipRepository.updateFollowing(accountId, true)
                accountRepository.updateFollowingCountInDatabase(loggedInId, -1)
                relationshipRepository.updateFollowing(accountId, false)
            }
        }

    @Test
    fun outerScopeCancelledTest() {
        val accountId = "id1"
        val loggedInId = "id2"

        testOuterScopeCancelled(
            delayedCallBlock = {
                relationshipRepository.updateFollowing(any(), true)
            },
            subjectCallBlock = {
                subject(
                    accountId = accountId,
                    loggedInUserAccountId = loggedInId,
                )
            },
            verifyBlock = {
                accountRepository.followAccount(accountId)
            },
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                relationshipRepository.updateFollowing(any(), true)
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
