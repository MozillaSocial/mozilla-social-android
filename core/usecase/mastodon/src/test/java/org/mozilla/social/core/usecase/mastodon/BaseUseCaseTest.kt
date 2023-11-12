package org.mozilla.social.core.usecase.mastodon

import io.mockk.MockKMatcherScope
import io.mockk.MockKVerificationScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.dao.AccountTimelineStatusDao
import org.mozilla.social.core.database.dao.AccountsDao
import org.mozilla.social.core.database.dao.FederatedTimelineStatusDao
import org.mozilla.social.core.database.dao.HashTagTimelineStatusDao
import org.mozilla.social.core.database.dao.HashtagDao
import org.mozilla.social.core.database.dao.HomeTimelineStatusDao
import org.mozilla.social.core.database.dao.LocalTimelineStatusDao
import org.mozilla.social.core.database.dao.PollsDao
import org.mozilla.social.core.database.dao.RelationshipsDao
import org.mozilla.social.core.database.dao.StatusDao
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.AppRepository
import org.mozilla.social.core.repository.mastodon.InstanceRepository
import org.mozilla.social.core.repository.mastodon.MediaRepository
import org.mozilla.social.core.repository.mastodon.OauthRepository
import org.mozilla.social.core.repository.mastodon.ReportRepository
import org.mozilla.social.core.repository.mastodon.SearchRepository
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.storage.mastodon.LocalAccountRepository
import org.mozilla.social.core.usecase.mastodon.utils.TransactionUtils
import kotlin.test.BeforeTest
import kotlin.test.fail

open class BaseUseCaseTest {

    protected val accountRepository = mockk<AccountRepository>(relaxed = true)
    protected val localAccountRepository = mockk<LocalAccountRepository>(relaxed = true)
    protected val appRepository = mockk<AppRepository>(relaxed = true)
    protected val instanceRepository = mockk<InstanceRepository>(relaxed = true)
    protected val mediaRepository = mockk<MediaRepository>(relaxed = true)
    protected val oauthRepository = mockk<OauthRepository>(relaxed = true)
    protected val reportRepository = mockk<ReportRepository>(relaxed = true)
    protected val searchRepository = mockk<SearchRepository>(relaxed = true)
    protected val statusRepository = mockk<StatusRepository>(relaxed = true)
    protected val timelineRepository = mockk<TimelineRepository>(relaxed = true)


    protected val socialDatabase = mockk<SocialDatabase>(relaxed = true)

    protected val accountsDao = mockk<AccountsDao>(relaxed = true)
    protected val accountTimelineDao = mockk<AccountTimelineStatusDao>(relaxed = true)
    protected val federatedTimelineDao = mockk<FederatedTimelineStatusDao>(relaxed = true)
    protected val hashtagDao = mockk<HashtagDao>(relaxed = true)
    protected val hashTagTimelineDao = mockk<HashTagTimelineStatusDao>(relaxed = true)
    protected val homeTimelineDao = mockk<HomeTimelineStatusDao>(relaxed = true)
    protected val localTimelineDao = mockk<LocalTimelineStatusDao>(relaxed = true)
    protected val pollsDao = mockk<PollsDao>(relaxed = true)
    protected val relationshipsDao = mockk<RelationshipsDao>(relaxed = true)
    protected val statusDao = mockk<StatusDao>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    protected val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    protected val showSnackbar = mockk<ShowSnackbar>(relaxed = true)

    @BeforeTest
    fun setupBaseTest() {
        every { socialDatabase.accountsDao() } returns accountsDao
        every { socialDatabase.accountTimelineDao() } returns accountTimelineDao
        every { socialDatabase.federatedTimelineDao() } returns federatedTimelineDao
        every { socialDatabase.hashtagDao() } returns hashtagDao
        every { socialDatabase.hashTagTimelineDao() } returns hashTagTimelineDao
        every { socialDatabase.homeTimelineDao() } returns homeTimelineDao
        every { socialDatabase.localTimelineDao() } returns localTimelineDao
        every { socialDatabase.pollDao() } returns pollsDao
        every { socialDatabase.relationshipsDao() } returns relationshipsDao
        every { socialDatabase.statusDao() } returns statusDao

        TransactionUtils.setupTransactionMock(socialDatabase)
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

        val outerJob = launch {
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
            throw TestException()
        }

        val outerJob = launch {
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