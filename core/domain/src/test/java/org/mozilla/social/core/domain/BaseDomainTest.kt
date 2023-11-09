package org.mozilla.social.core.domain

import io.mockk.MockKMatcherScope
import io.mockk.MockKVerificationScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
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
import org.mozilla.social.core.domain.utils.TransactionUtils
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.network.AccountApi
import org.mozilla.social.core.network.AppApi
import org.mozilla.social.core.network.InstanceApi
import org.mozilla.social.core.network.MediaApi
import org.mozilla.social.core.network.OauthApi
import org.mozilla.social.core.network.RecommendationApi
import org.mozilla.social.core.network.ReportApi
import org.mozilla.social.core.network.SearchApi
import org.mozilla.social.core.network.StatusApi
import org.mozilla.social.core.network.TimelineApi
import kotlin.test.BeforeTest

open class BaseDomainTest {

    protected val accountApi = mockk<AccountApi>(relaxed = true)
    protected val appApi = mockk<AppApi>(relaxed = true)
    protected val instanceApi = mockk<InstanceApi>(relaxed = true)
    protected val mediaApi = mockk<MediaApi>(relaxed = true)
    protected val oauthApi = mockk<OauthApi>(relaxed = true)
    protected val recommendationApi = mockk<RecommendationApi>(relaxed = true)
    protected val reportApi = mockk<ReportApi>(relaxed = true)
    protected val searchApi = mockk<SearchApi>(relaxed = true)
    protected val statusApi = mockk<StatusApi>(relaxed = true)
    protected val timelineApi = mockk<TimelineApi>(relaxed = true)

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
    protected val testDispatcher = UnconfinedTestDispatcher()

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
        val mutex1 = Mutex(locked = true)
        val mutex2 = Mutex(locked = true)

        coEvery(delayedCallBlock).coAnswers {
            CoroutineScope(Dispatchers.Default).launch {
                // we know we've started the subject block, so we can unlock the first mutex
                mutex1.unlock()
                println("lock 1 unlocked")
                delay(100)
                // unlock to allow the verify block to run
                mutex2.unlock()
                println("lock 2 unlocked")
            }.join()
            delayedCallBlockReturnValue
        }

        val job = CoroutineScope(Dispatchers.Default).launch {
            subjectCallBlock()
        }

        // lock to make sure we give the subject callback time to start
        println("lock 1 locking")
        mutex1.lock()

        job.cancel()

        // lock again to make sure our delayed callback has run before we verify
        println("lock 2 locking")
        mutex2.lock()

        // delay to make sure the rest of the subject block has had time to run
        delay(50)

        coVerify(exactly = 1, verifyBlock = verifyBlock)
    }
}