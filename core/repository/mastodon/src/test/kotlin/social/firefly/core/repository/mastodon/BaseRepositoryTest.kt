package social.firefly.core.repository.mastodon

import io.mockk.every
import io.mockk.mockk
import social.firefly.core.database.SocialDatabase
import social.firefly.core.database.dao.AccountTimelineStatusDao
import social.firefly.core.database.dao.AccountsDao
import social.firefly.core.database.dao.FederatedTimelineStatusDao
import social.firefly.core.database.dao.HashTagTimelineStatusDao
import social.firefly.core.database.dao.HomeTimelineStatusDao
import social.firefly.core.database.dao.LocalTimelineStatusDao
import social.firefly.core.database.dao.PollsDao
import social.firefly.core.database.dao.RelationshipsDao
import social.firefly.core.database.dao.StatusDao
import social.firefly.core.network.mastodon.AccountApi
import social.firefly.core.network.mastodon.AppApi
import social.firefly.core.network.mastodon.InstanceApi
import social.firefly.core.network.mastodon.MediaApi
import social.firefly.core.network.mastodon.ReportApi
import social.firefly.core.network.mastodon.SearchApi
import social.firefly.core.network.mastodon.StatusApi
import social.firefly.core.network.mastodon.TimelineApi
import kotlin.test.BeforeTest

open class BaseRepositoryTest {
    protected val accountApi = mockk<AccountApi>(relaxed = true)
    protected val appApi = mockk<AppApi>(relaxed = true)
    protected val instanceApi = mockk<InstanceApi>(relaxed = true)
    protected val mediaApi = mockk<MediaApi>(relaxed = true)
    protected val reportApi = mockk<ReportApi>(relaxed = true)
    protected val searchApi = mockk<SearchApi>(relaxed = true)
    protected val statusApi = mockk<StatusApi>(relaxed = true)
    protected val timelineApi = mockk<TimelineApi>(relaxed = true)

    protected val socialDatabase = mockk<SocialDatabase>(relaxed = true)
    protected val pollsDao = mockk<PollsDao>(relaxed = true)

    protected val accountsDao = mockk<AccountsDao>(relaxed = true)
    protected val accountTimelineDao = mockk<AccountTimelineStatusDao>(relaxed = true)
    protected val federatedTimelineDao = mockk<FederatedTimelineStatusDao>(relaxed = true)
    protected val hashTagTimelineDao = mockk<HashTagTimelineStatusDao>(relaxed = true)
    protected val homeTimelineDao = mockk<HomeTimelineStatusDao>(relaxed = true)
    protected val localTimelineDao = mockk<LocalTimelineStatusDao>(relaxed = true)
    protected val relationshipsDao = mockk<RelationshipsDao>(relaxed = true)
    protected val statusDao = mockk<StatusDao>(relaxed = true)

    @BeforeTest
    fun setupBaseTest() {
        every { socialDatabase.accountsDao() } returns accountsDao
        every { socialDatabase.accountTimelineDao() } returns accountTimelineDao
        every { socialDatabase.federatedTimelineDao() } returns federatedTimelineDao
        every { socialDatabase.hashTagTimelineDao() } returns hashTagTimelineDao
        every { socialDatabase.homeTimelineDao() } returns homeTimelineDao
        every { socialDatabase.localTimelineDao() } returns localTimelineDao
        every { socialDatabase.relationshipsDao() } returns relationshipsDao
        every { socialDatabase.statusDao() } returns statusDao

        TransactionUtils.setupTransactionMock(socialDatabase)
    }
}
