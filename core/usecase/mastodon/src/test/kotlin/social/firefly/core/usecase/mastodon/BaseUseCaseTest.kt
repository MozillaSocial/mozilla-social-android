package social.firefly.core.usecase.mastodon

import io.mockk.MockKMatcherScope
import io.mockk.MockKVerificationScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.FavoritesRepository
import social.firefly.core.repository.mastodon.HashtagRepository
import social.firefly.core.repository.mastodon.InstanceRepository
import social.firefly.core.repository.mastodon.MediaRepository
import social.firefly.core.repository.mastodon.PollRepository
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.repository.mastodon.ReportRepository
import social.firefly.core.repository.mastodon.SearchRepository
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase
import social.firefly.core.usecase.mastodon.utils.TransactionUtils
import kotlin.test.BeforeTest
import kotlin.test.fail

open class BaseUseCaseTest {
    protected val accountRepository = mockk<AccountRepository>(relaxed = true)
    protected val instanceRepository = mockk<InstanceRepository>(relaxed = true)
    protected val mediaRepository = mockk<MediaRepository>(relaxed = true)
    protected val relationshipRepository = mockk<RelationshipRepository>(relaxed = true)
    protected val reportRepository = mockk<ReportRepository>(relaxed = true)
    protected val searchRepository = mockk<SearchRepository>(relaxed = true)
    protected val statusRepository = mockk<StatusRepository>(relaxed = true)
    protected val pollRepository = mockk<PollRepository>(relaxed = true)
    protected val timelineRepository = mockk<TimelineRepository>(relaxed = true)
    protected val favoritesRepository = mockk<FavoritesRepository>(relaxed = true)
    protected val hashtagRepository = mockk<HashtagRepository>(relaxed = true)

    protected val databaseDelegate = mockk<DatabaseDelegate>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    protected val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    internal val saveStatusToDatabase = mockk<SaveStatusToDatabase>(relaxed = true)

    protected val showSnackbar = mockk<ShowSnackbar>(relaxed = true)

    @BeforeTest
    fun setupBaseTest() {
        TransactionUtils.setupTransactionMock(databaseDelegate)
    }

    /**
     * Used for use cases that should continue running even if their outer scope gets cancelled
     * @param delayedCallBlock a block of a call that should be slightly delayed.  For example,
     * if your use case uses [AccountApi.followAccount], then this parameter should look like
     * { accountApi.followAccount(any()) }
     * @param subjectCallBlock a block that is calling you use case
     * @param verifyBlock a block that we should verify gets called once.  Typically good to use
     * the same call that you used in the @param delayedCallBlock.
     */
    protected fun testOuterScopeCancelled(
        delayedCallBlock: suspend MockKMatcherScope.() -> Any,
        delayedCallBlockReturnValue: Any = Unit,
        subjectCallBlock: suspend () -> Unit,
        verifyBlock: suspend MockKVerificationScope.() -> Unit,
    ) = runTest {
        val waitToCancel = CompletableDeferred<Unit>()
        val waitToFinish = CompletableDeferred<Unit>()

        coEvery(delayedCallBlock).coAnswers {
            println("allow cancel")
            waitToCancel.complete(Unit)
            println("wait to finish")
            waitToFinish.await()
            println("finishing subject block")
            delayedCallBlockReturnValue
        }

        val outerJob =
            launch {
                subjectCallBlock()
            }

        println("wait to cancel")
        waitToCancel.await()

        println("canceling outer job")
        outerJob.cancel()
        println("allow finish")
        waitToFinish.complete(Unit)

        println("verify")
        coVerify(exactly = 1, verifyBlock = verifyBlock)
    }

    /**
     * When the outer job is canceled, but an exception is thrown inside, it should not be
     * caught in the outer job's catch.
     */
    @Suppress("TooGenericExceptionThrown", "SwallowedException")
    protected fun testOuterScopeCancelledAndInnerException(
        delayedCallBlock: suspend MockKMatcherScope.() -> Any,
        subjectCallBlock: suspend () -> Unit,
        verifyBlock: suspend MockKVerificationScope.() -> Unit,
    ) = runTest {
        val waitToCancel = CompletableDeferred<Unit>()
        val waitToFinish = CompletableDeferred<Unit>()

        coEvery(delayedCallBlock).coAnswers {
            println("allow cancel")
            waitToCancel.complete(Unit)
            println("wait to finish")
            waitToFinish.await()
            println("finishing subject block")
            throw social.firefly.core.usecase.mastodon.TestException()
        }

        val outerJob =
            launch {
                try {
                    subjectCallBlock()
                } catch (e: CancellationException) {
                    println("canceled")
                } catch (e: Exception) {
                    // fail the test if the exception gets caught here
                    // the outer job should have already been canceled
                    println("failing $e")
                    fail("The exception should have been caught outside this scope")
                }
            }

        println("wait to cancel")
        waitToCancel.await()
        println("canceling outer job")
        outerJob.cancel()
        println("allow finish")
        waitToFinish.complete(Unit)

        println("verify")
        coVerify(exactly = 1, verifyBlock = verifyBlock)
    }
}

class TestException : Exception()
